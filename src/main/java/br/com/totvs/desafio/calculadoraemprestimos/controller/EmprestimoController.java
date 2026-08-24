package br.com.totvs.desafio.calculadoraemprestimos.controller;

import br.com.totvs.desafio.calculadoraemprestimos.dto.CalculoEmprestimoRequest;
import br.com.totvs.desafio.calculadoraemprestimos.dto.CalculoEmprestimoResponse;
import br.com.totvs.desafio.calculadoraemprestimos.service.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping("/calcular")
    public CalculoEmprestimoResponse calcular(@Valid @RequestBody CalculoEmprestimoRequest request) {
        return emprestimoService.calcular(request);
    }
}
