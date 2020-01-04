import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, of } from "rxjs";
import { QueryParams, ResultVO, TableQueryParams, TableResultVO } from "../model/result-vm";
import { CustomerVO } from "../model/customer";
import { AppConfig } from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  constructor(
    private http: HttpClient
  ) {

  }

  public find(id: number): Observable<ResultVO<CustomerVO>> {
    return this.http.get<ResultVO<CustomerVO>>(`${AppConfig.BASE_URL}/api/customer/${id}`);
    // return of({
    //   code: 200,
    //   message: '',
    //   data: {
    //     id: 1,
    //     name: 'XXX公司',
    //     shorthand: '',
    //     type: 1,
    //     period: 1,
    //     payDate: 20,
    //     description: '',
    //
    //     specialPrices: [{
    //       id: 0,
    //       productId: 0,
    //       productName: '铝棒',
    //       price: 1000,
    //       priceType: 1
    //     }, {
    //       id: 2,
    //       productId: 2,
    //       productName: '铝条',
    //       price: 2000,
    //       priceType: 2
    //     }]
    //   }
    // });
  }

  public findAll(queryParams: TableQueryParams): Observable<ResultVO<TableResultVO<CustomerVO>>>;

  public findAll(queryParams: QueryParams): Observable<ResultVO<CustomerVO[]>>;

  public findAll(
    queryParams: QueryParams | TableQueryParams
  ): Observable<ResultVO<CustomerVO[]>>
    | Observable<ResultVO<TableResultVO<CustomerVO>>> {
    if (queryParams instanceof TableQueryParams) {
      return this.http.get<ResultVO<TableResultVO<CustomerVO>>>(`${AppConfig.BASE_URL}/api/customer/list`, {
        params: queryParams
      });
      // return of({
      //   code: 200,
      //   message: '',
      //   data: {
      //     totalPages: 1,
      //     pageIndex: 1,
      //     pageSize: 1,
      //     result: [{
      //       id: 1,
      //       name: 'XXX公司',
      //       shorthand: '',
      //       type: 1,
      //       period: 1,
      //       payDate: 20,
      //       description: ''
      //     }]
      //   }
      // });
    } else {
      return of({
        code: 200,
        message: '',
        data: [{
          id: 1,
          name: 'XXX公司',
          shorthand: '',
          type: 1,
          period: 1,
          payDate: 20,
          description: ''
        }]
      });
    }
  }

  public save(customer: CustomerVO): Observable<ResultVO<any>> {
    // return of(null);
    return this.http.post<ResultVO<any>>(`${AppConfig.BASE_URL}/api/customer`, customer);
  }

  public delete(id: number): Observable<ResultVO<any>> {
    // return of(null);
    return this.http.get<ResultVO<any>>(`${AppConfig.BASE_URL}/api/customer/delete/${id}`);
  }

}
