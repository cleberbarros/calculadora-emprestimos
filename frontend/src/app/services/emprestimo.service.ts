import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CalculoEmprestimoRequest } from '../models/calculo-emprestimo-request';
import { CalculoEmprestimoResponse } from '../models/calculo-emprestimo-response';

@Injectable({ providedIn: 'root' })
export class EmprestimoService {

  private readonly url = 'http://localhost:8080/api/emprestimos/calcular';

  constructor(private http: HttpClient) {}

  calcular(request: CalculoEmprestimoRequest): Observable<CalculoEmprestimoResponse> {
    return this.http.post<CalculoEmprestimoResponse>(this.url, request);
  }
}
