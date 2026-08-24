package br.com.totvs.desafio.calculadoraemprestimos.service;

import br.com.totvs.desafio.calculadoraemprestimos.dto.CalculoEmprestimoRequest;
import br.com.totvs.desafio.calculadoraemprestimos.dto.CalculoEmprestimoResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class EmprestimoService {

    public CalculoEmprestimoResponse calcular(CalculoEmprestimoRequest request) {
        validarDatas(request);

        List<LocalDate> datasCalculo = gerarDatasCalculo(request);
        int quantidadeParcelas = calcularQuantidadeParcelas(request);

        // TODO: Implementar lógica financeira de cálculo do empréstimo
        throw new UnsupportedOperationException("Cálculo de empréstimo ainda não implementado");
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

    private int calcularQuantidadeParcelas(CalculoEmprestimoRequest request) {
        return gerarDatasPagamento(request).size();
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
}
