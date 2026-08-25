export interface CalculoEmprestimoRequest {
  dataInicial: string;
  dataFinal: string;
  primeiroPagamento: string;
  valorEmprestimo: number;
  taxaJuros: number;
}
