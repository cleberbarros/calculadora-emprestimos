package br.com.totvs.desafio.calculadoraemprestimos.service;

import br.com.totvs.desafio.calculadoraemprestimos.dto.CalculoEmprestimoRequest;
import br.com.totvs.desafio.calculadoraemprestimos.dto.CalculoEmprestimoResponse;
import org.springframework.stereotype.Service;

@Service
public class EmprestimoService {

    public CalculoEmprestimoResponse calcular(CalculoEmprestimoRequest request) {
        validarDatas(request);

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
}
