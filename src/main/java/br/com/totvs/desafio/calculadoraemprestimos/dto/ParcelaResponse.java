package br.com.totvs.desafio.calculadoraemprestimos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ParcelaResponse {

    private LocalDate data;
    private BigDecimal valorEmprestimo;
    private BigDecimal saldoDevedor;
    private BigDecimal parcela;
    private BigDecimal total;
    private BigDecimal amortizacao;
    private BigDecimal saldoPrincipal;
    private BigDecimal provisao;
    private BigDecimal jurosAcumulados;
    private BigDecimal jurosPagos;

    public ParcelaResponse() {
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getValorEmprestimo() {
        return valorEmprestimo;
    }

    public void setValorEmprestimo(BigDecimal valorEmprestimo) {
        this.valorEmprestimo = valorEmprestimo;
    }

    public BigDecimal getSaldoDevedor() {
        return saldoDevedor;
    }

    public void setSaldoDevedor(BigDecimal saldoDevedor) {
        this.saldoDevedor = saldoDevedor;
    }

    public BigDecimal getParcela() {
        return parcela;
    }

    public void setParcela(BigDecimal parcela) {
        this.parcela = parcela;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getAmortizacao() {
        return amortizacao;
    }

    public void setAmortizacao(BigDecimal amortizacao) {
        this.amortizacao = amortizacao;
    }

    public BigDecimal getSaldoPrincipal() {
        return saldoPrincipal;
    }

    public void setSaldoPrincipal(BigDecimal saldoPrincipal) {
        this.saldoPrincipal = saldoPrincipal;
    }

    public BigDecimal getProvisao() {
        return provisao;
    }

    public void setProvisao(BigDecimal provisao) {
        this.provisao = provisao;
    }

    public BigDecimal getJurosAcumulados() {
        return jurosAcumulados;
    }

    public void setJurosAcumulados(BigDecimal jurosAcumulados) {
        this.jurosAcumulados = jurosAcumulados;
    }

    public BigDecimal getJurosPagos() {
        return jurosPagos;
    }

    public void setJurosPagos(BigDecimal jurosPagos) {
        this.jurosPagos = jurosPagos;
    }
}
