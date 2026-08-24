package br.com.totvs.desafio.calculadoraemprestimos.service;

import br.com.totvs.desafio.calculadoraemprestimos.dto.CalculoEmprestimoRequest;
import br.com.totvs.desafio.calculadoraemprestimos.dto.CalculoEmprestimoResponse;
import br.com.totvs.desafio.calculadoraemprestimos.dto.ParcelaResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class EmprestimoService {

    private static final BigDecimal BASE_DIAS = BigDecimal.valueOf(360);
    private static final MathContext MC = new MathContext(15, RoundingMode.HALF_UP);

    public CalculoEmprestimoResponse calcular(CalculoEmprestimoRequest request) {
        validarDatas(request);

        List<LocalDate> datasCalculo = gerarDatasCalculo(request);
        Set<LocalDate> datasPagamento = gerarDatasPagamento(request);
        int quantidadeParcelas = datasPagamento.size();

        List<ParcelaResponse> parcelas = calcularParcelas(
                request,
                datasCalculo,
                datasPagamento,
                quantidadeParcelas);

        CalculoEmprestimoResponse response = new CalculoEmprestimoResponse();
        response.setQuantidadeParcelas(quantidadeParcelas);
        response.setParcelas(parcelas);

        return response;
    }

    private void validarDatas(CalculoEmprestimoRequest request) {
        if (!request.getDataFinal().isAfter(request.getDataInicial())) {
            throw new IllegalArgumentException("A data final deve ser posterior à data inicial.");
        }

        if (!request.getPrimeiroPagamento().isAfter(request.getDataInicial())
                || !request.getPrimeiroPagamento().isBefore(request.getDataFinal())) {
            throw new IllegalArgumentException(
                    "A data do primeiro pagamento deve ser posterior à data inicial e anterior à data final.");
        }
    }

    private List<LocalDate> gerarDatasCalculo(CalculoEmprestimoRequest request) {
        Set<LocalDate> datas = new TreeSet<>();

        LocalDate dataInicial = request.getDataInicial();
        LocalDate dataFinal = request.getDataFinal();

        datas.add(dataInicial);

        LocalDate data = dataInicial.with(TemporalAdjusters.lastDayOfMonth());

        while (data.isBefore(dataFinal)) {
            if (data.isAfter(dataInicial)) {
                datas.add(data);
            }

            data = data.plusMonths(1)
                    .with(TemporalAdjusters.lastDayOfMonth());
        }

        datas.addAll(gerarDatasPagamento(request));

        return new ArrayList<>(datas);
    }

    private Set<LocalDate> gerarDatasPagamento(CalculoEmprestimoRequest request) {
        Set<LocalDate> pagamentos = new TreeSet<>();

        LocalDate primeiroPagamento = request.getPrimeiroPagamento();
        LocalDate dataFinal = request.getDataFinal();

        int diaPagamento = primeiroPagamento.getDayOfMonth();
        LocalDate pagamento = primeiroPagamento;

        while (pagamento.isBefore(dataFinal)) {
            pagamentos.add(pagamento);

            YearMonth mes = YearMonth.from(pagamento).plusMonths(1);
            int dia = Math.min(diaPagamento, mes.lengthOfMonth());

            pagamento = mes.atDay(dia);
        }

        pagamentos.add(dataFinal);

        return pagamentos;
    }

    private List<ParcelaResponse> calcularParcelas(
            CalculoEmprestimoRequest request,
            List<LocalDate> datasCalculo,
            Set<LocalDate> datasPagamento,
            int quantidadeParcelas) {

        List<ParcelaResponse> parcelas = new ArrayList<>();

        BigDecimal valorEmprestimo = request.getValorEmprestimo();
        BigDecimal taxaJuros = request.getTaxaJuros()
                .divide(BigDecimal.valueOf(100), MC);

        BigDecimal amortizacaoBase = valorEmprestimo
                .divide(BigDecimal.valueOf(quantidadeParcelas), MC);

        BigDecimal saldoPrincipal = valorEmprestimo;
        BigDecimal jurosAcumulados = BigDecimal.ZERO;

        LocalDate dataAnterior = null;

        for (LocalDate data : datasCalculo) {

            BigDecimal provisao = BigDecimal.ZERO;
            BigDecimal amortizacao = BigDecimal.ZERO;
            BigDecimal jurosPagos = BigDecimal.ZERO;

            if (dataAnterior != null) {
                long dias = ChronoUnit.DAYS.between(dataAnterior, data);

                double expoente = BigDecimal.valueOf(dias)
                        .divide(BASE_DIAS, MC)
                        .doubleValue();

                double fator = Math.pow(
                        BigDecimal.ONE.add(taxaJuros).doubleValue(),
                        expoente) - 1;

                BigDecimal baseJuros = saldoPrincipal
                        .add(jurosAcumulados);

                provisao = baseJuros.multiply(
                        BigDecimal.valueOf(fator), MC);
            }

            boolean ehPagamento = datasPagamento.contains(data);

            if (ehPagamento) {
                amortizacao = amortizacaoBase;

                jurosPagos = jurosAcumulados
                        .add(provisao, MC);

                jurosAcumulados = BigDecimal.ZERO;

                saldoPrincipal = saldoPrincipal
                        .subtract(amortizacao, MC);
            } else {
                jurosAcumulados = jurosAcumulados
                        .add(provisao, MC);
            }

            BigDecimal saldoDevedor = saldoPrincipal
                    .add(jurosAcumulados, MC);

            BigDecimal total = amortizacao
                    .add(jurosPagos, MC);

            ParcelaResponse parcela = new ParcelaResponse();
            parcela.setData(data);
            parcela.setValorEmprestimo(valorEmprestimo);
            parcela.setSaldoDevedor(saldoDevedor);
            parcela.setParcela(ehPagamento ? amortizacao : BigDecimal.ZERO);
            parcela.setTotal(total);
            parcela.setAmortizacao(amortizacao);
            parcela.setSaldoPrincipal(saldoPrincipal);
            parcela.setProvisao(provisao);
            parcela.setJurosAcumulados(jurosAcumulados);
            parcela.setJurosPagos(jurosPagos);

            parcelas.add(parcela);

            dataAnterior = data;
        }

        return parcelas;
    }
}
