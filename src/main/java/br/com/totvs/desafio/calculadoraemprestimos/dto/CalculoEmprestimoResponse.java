package br.com.totvs.desafio.calculadoraemprestimos.dto;

import java.util.List;

public class CalculoEmprestimoResponse {

    private Integer quantidadeParcelas;
    private List<ParcelaResponse> parcelas;

    public CalculoEmprestimoResponse() {
    }

    public Integer getQuantidadeParcelas() {
        return quantidadeParcelas;
    }

    public void setQuantidadeParcelas(Integer quantidadeParcelas) {
        this.quantidadeParcelas = quantidadeParcelas;
    }

    public List<ParcelaResponse> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<ParcelaResponse> parcelas) {
        this.parcelas = parcelas;
    }
}
