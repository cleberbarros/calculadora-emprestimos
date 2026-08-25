import { Component, LOCALE_ID } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CurrencyPipe, DatePipe, registerLocaleData } from '@angular/common';
import localePtBr from '@angular/common/locales/pt';
import { EmprestimoService } from './services/emprestimo.service';
import { CalculoEmprestimoRequest } from './models/calculo-emprestimo-request';
import { CalculoEmprestimoResponse } from './models/calculo-emprestimo-response';

registerLocaleData(localePtBr, 'pt-BR');

@Component({
  selector: 'app-root',
  imports: [ReactiveFormsModule, CurrencyPipe, DatePipe],
  providers: [{ provide: LOCALE_ID, useValue: 'pt-BR' }],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  formulario: FormGroup;
  resultado: CalculoEmprestimoResponse | null = null;
  erro: string | null = null;
  carregando = false;

  constructor(
    private fb: FormBuilder,
    private emprestimoService: EmprestimoService
  ) {
    this.formulario = this.fb.group({
      dataInicial: ['', Validators.required],
      dataFinal: ['', Validators.required],
      primeiroPagamento: ['', Validators.required],
      valorEmprestimo: [null, [Validators.required, Validators.min(0.01)]],
      taxaJuros: [null, [Validators.required, Validators.min(0.01)]]
    });
  }

  calcular(): void {
    this.erro = null;
    this.resultado = null;

    if (this.formulario.invalid) {
      return;
    }

    const { dataInicial, dataFinal, primeiroPagamento } = this.formulario.value;

    if (dataFinal <= dataInicial) {
      this.erro = 'A data final deve ser posterior à data inicial.';
      return;
    }

    if (primeiroPagamento <= dataInicial || primeiroPagamento >= dataFinal) {
      this.erro = 'O primeiro pagamento deve ser posterior à data inicial e anterior à data final.';
      return;
    }

    const request: CalculoEmprestimoRequest = {
      dataInicial,
      dataFinal,
      primeiroPagamento,
      valorEmprestimo: this.formulario.value.valorEmprestimo,
      taxaJuros: this.formulario.value.taxaJuros
    };

    this.carregando = true;

    this.emprestimoService.calcular(request).subscribe({
      next: (response) => {
        this.resultado = response;
        this.carregando = false;
      },
      error: () => {
        this.erro = 'Não foi possível realizar o cálculo.';
        this.carregando = false;
      }
    });
  }
}
