export interface ParcelaResponse {
  data: string;
  valorEmprestimo: number;
  saldoDevedor: number;
  parcela: number;
  total: number;
  amortizacao: number;
  saldoPrincipal: number;
  provisao: number;
  jurosAcumulados: number;
  jurosPagos: number;
}
