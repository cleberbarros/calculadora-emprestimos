import { ParcelaResponse } from './parcela-response';

export interface CalculoEmprestimoResponse {
  quantidadeParcelas: number;
  parcelas: ParcelaResponse[];
}
