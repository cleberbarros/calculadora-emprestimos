package br.com.totvs.desafio.calculadoraemprestimos.service;

import br.com.totvs.desafio.calculadoraemprestimos.dto.CalculoEmprestimoRequest;
import br.com.totvs.desafio.calculadoraemprestimos.dto.CalculoEmprestimoResponse;
import br.com.totvs.desafio.calculadoraemprestimos.dto.ParcelaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmprestimoServiceTest {

    private EmprestimoService service;

    @BeforeEach
    void setUp() {
        service = new EmprestimoService();
    }

    private CalculoEmprestimoRequest criarRequestPlanilha() {
        CalculoEmprestimoRequest request = new CalculoEmprestimoRequest();
        request.setDataInicial(LocalDate.of(2024, 1, 1));
        request.setDataFinal(LocalDate.of(2034, 1, 1));
        request.setPrimeiroPagamento(LocalDate.of(2024, 2, 15));
        request.setValorEmprestimo(new BigDecimal("140000"));
        request.setTaxaJuros(new BigDecimal("7"));
        return request;
    }

    @Test
    void calcularEmprestimoComCenarioDaPlanilha() {
        CalculoEmprestimoRequest request = criarRequestPlanilha();

        CalculoEmprestimoResponse response = service.calcular(request);

        assertEquals(120, response.getQuantidadeParcelas());
        assertFalse(response.getParcelas().isEmpty());

        ParcelaResponse primeira = response.getParcelas().get(0);
        assertEquals(LocalDate.of(2024, 1, 1), primeira.getData());
        assertEquals(0, new BigDecimal("140000").compareTo(primeira.getSaldoPrincipal()));
        assertEquals(0, new BigDecimal("140000").compareTo(primeira.getSaldoDevedor()));
        assertEquals(0, BigDecimal.ZERO.compareTo(primeira.getAmortizacao()));
        assertEquals(0, BigDecimal.ZERO.compareTo(primeira.getProvisao()));
        assertEquals(0, BigDecimal.ZERO.compareTo(primeira.getJurosAcumulados()));
        assertEquals(0, BigDecimal.ZERO.compareTo(primeira.getJurosPagos()));
    }

    @Test
    void finalizarEmprestimoComSaldoZerado() {
        CalculoEmprestimoRequest request = criarRequestPlanilha();

        CalculoEmprestimoResponse response = service.calcular(request);

        List<ParcelaResponse> parcelas = response.getParcelas();
        ParcelaResponse ultima = parcelas.get(parcelas.size() - 1);

        assertEquals(LocalDate.of(2034, 1, 1), ultima.getData());
        assertEquals(0, BigDecimal.ZERO.compareTo(ultima.getSaldoPrincipal()));
        assertEquals(0, BigDecimal.ZERO.compareTo(ultima.getSaldoDevedor()));
    }

    @Test
    void calcularPrimeiroPagamentoCorretamente() {
        CalculoEmprestimoRequest request = criarRequestPlanilha();

        CalculoEmprestimoResponse response = service.calcular(request);

        ParcelaResponse pagamento = response.getParcelas().stream()
                .filter(p -> p.getData().equals(LocalDate.of(2024, 2, 15)))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Parcela de 2024-02-15 não encontrada"));

        BigDecimal tolerancia = new BigDecimal("0.000001");

        assertAproximado(new BigDecimal("1166.66666666667"), pagamento.getAmortizacao(), tolerancia);
        assertAproximado(new BigDecimal("397.466990220909"), pagamento.getProvisao(), tolerancia);
        assertAproximado(new BigDecimal("1189.04734445765"), pagamento.getJurosPagos(), tolerancia);
        assertAproximado(new BigDecimal("2355.71401112432"), pagamento.getTotal(), tolerancia);
        assertAproximado(new BigDecimal("138833.333333333"), pagamento.getSaldoPrincipal(), tolerancia);
    }

    @Test
    void ajustarPagamentoParaUltimoDiaDoMes() {
        CalculoEmprestimoRequest request = new CalculoEmprestimoRequest();
        request.setDataInicial(LocalDate.of(2024, 1, 1));
        request.setDataFinal(LocalDate.of(2025, 1, 31));
        request.setPrimeiroPagamento(LocalDate.of(2024, 1, 31));
        request.setValorEmprestimo(new BigDecimal("120000"));
        request.setTaxaJuros(new BigDecimal("5"));

        CalculoEmprestimoResponse response = service.calcular(request);

        List<LocalDate> datasComPagamento = response.getParcelas().stream()
                .filter(p -> p.getAmortizacao().compareTo(BigDecimal.ZERO) > 0)
                .map(ParcelaResponse::getData)
                .toList();

        assertTrue(datasComPagamento.contains(LocalDate.of(2024, 1, 31)));
        assertTrue(datasComPagamento.contains(LocalDate.of(2024, 2, 29)));
        assertTrue(datasComPagamento.contains(LocalDate.of(2024, 3, 31)));
    }

    @Test
    void rejeitarDataFinalAnteriorADataInicial() {
        CalculoEmprestimoRequest request = new CalculoEmprestimoRequest();
        request.setDataInicial(LocalDate.of(2024, 6, 1));
        request.setDataFinal(LocalDate.of(2024, 1, 1));
        request.setPrimeiroPagamento(LocalDate.of(2024, 3, 1));
        request.setValorEmprestimo(new BigDecimal("100000"));
        request.setTaxaJuros(new BigDecimal("5"));

        assertThrows(IllegalArgumentException.class, () -> service.calcular(request));
    }

    @Test
    void rejeitarPrimeiroPagamentoForaDoPeriodo() {
        CalculoEmprestimoRequest request = new CalculoEmprestimoRequest();
        request.setDataInicial(LocalDate.of(2024, 1, 1));
        request.setDataFinal(LocalDate.of(2025, 1, 1));
        request.setPrimeiroPagamento(LocalDate.of(2025, 6, 1));
        request.setValorEmprestimo(new BigDecimal("100000"));
        request.setTaxaJuros(new BigDecimal("5"));

        assertThrows(IllegalArgumentException.class, () -> service.calcular(request));
    }

    private void assertAproximado(BigDecimal esperado, BigDecimal atual, BigDecimal tolerancia) {
        BigDecimal diferenca = esperado.subtract(atual).abs();
        assertTrue(diferenca.compareTo(tolerancia) <= 0,
                String.format("Esperado ~%s mas obteve %s (diferença: %s)", esperado, atual, diferenca));
    }
}
