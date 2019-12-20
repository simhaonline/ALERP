import { Component, OnInit } from "@angular/core";
import { CustomerService } from "../../../../core/services/customer.service";
import { ActivatedRoute, Router } from "@angular/router";
import { ResultVO } from "../../../../core/model/result-vm";
import { CustomerSpecialPriceVO, CustomerVO } from "../../../../core/model/customer";
import { Objects, StringUtils } from "../../../../core/services/util.service";
import { HttpErrorResponse } from "@angular/common/http";
import { NzMessageService } from "ng-zorro-antd";
import { ProductVO } from "../../../../core/model/product";
import { BehaviorSubject, Observable } from "rxjs";
import { ProductService } from "../../../../core/services/product.service";
import { debounceTime, map, switchMap } from "rxjs/operators";

@Component({
  selector: 'customer-info',
  templateUrl: './customer-info.component.html',
  styleUrls: [ './customer-info.component.less' ]
})
export class CustomerInfoComponent implements OnInit {

  isLoading: boolean = true;
  customerId: number;
  customerData: CustomerVO;
  customerDataValidate: any = {
    name: null,
    period: null,
    payDate: null
  };

  customerDataCache: CustomerVO;

  isInfoEditing: boolean = false;
  isInfoSaving: boolean = false;

  editCache: {
    _id?: number,
    data?: TempCustomerSpecialPriceVO,
    product?: TempProductVO,
    currentProduct?: TempProductVO,
    isAdd?: boolean
  } = {};
  defaultEditCache: any = {
    _id: null,
    data: null,
    product: null,
    currentProduct: null,
    isAdd: false
  };
  customerSpecialPriceCountIndex: number = 0;
  searchProducts: ProductVO[];
  searchChange$: BehaviorSubject<string> = new BehaviorSubject('');
  isProductsLoading: boolean = false;

  periodFormatter: any = (value: number) => {
    if (Objects.valid(value)) {
      return `${value} 个月`;
    } else {
      return ``;
    }
  };
  payDateFormatter: any = (value: number) => {
    if (Objects.valid(value)) {
      return `${value} 日`;
    } else {
      return ``;
    }
  };
  cashFormatter: any = (value: number) => {
    if (Objects.valid(value)) {
      return `¥ ${value}`;
    } else {
      return `¥`;
    }
  };

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private customer: CustomerService,
    private product: ProductService,
    private message: NzMessageService
  ) {

  }

  ngOnInit(): void {
    this.customerId = this.route.snapshot.params[ 'id' ];
    this.reload();

    const getProducts: any = (name: string) => {
      const t: Observable<ResultVO<ProductVO[]>>
        = <Observable<ResultVO<ProductVO[]>>>this.product
        .findAll({});
      return t.pipe(map(res => res.data));
    };
    const optionList$: Observable<ProductVO[]> = this.searchChange$
      .asObservable()
      .pipe(debounceTime(500))
      .pipe(switchMap(getProducts));
    optionList$.subscribe((data: ProductVO[]) => {
      this.searchProducts = data;
      this.isProductsLoading = false;
    });
    console.log(this.editCache);
  }

  startInfoEdit(): void {
    this.isInfoEditing = true;
    for (const k in this.customerDataValidate) {
      this.customerDataValidate[ k ] = null;
    }
    this.customerDataCache = {
      name: '',
      type: 1,
      period: 1,
      payDate: 20
    };
    Object.assign(this.customerDataCache, this.customerData);
    console.log(this.customerDataCache);
  }

  onInputValueChange(value: any, name: string): void {
    if (!Objects.valid(value) || StringUtils.isEmpty(value + '')) {
      this.customerDataValidate[ name ] = 'error';
    } else {
      this.customerDataValidate[ name ] = null;
    }
  }

  saveInfoEdit(): void {
    let valid: boolean = true;
    if (!Objects.valid(this.customerData.name)) {
      this.customerDataValidate.name = 'error';
      valid = false;
    }
    if (!Objects.valid(this.customerData.period)) {
      this.customerDataValidate.perid = 'error';
      valid = false;
    }
    if (!Objects.valid(this.customerData.name)) {
      this.customerDataValidate.name = 'error';
      valid = false;
    }
    if (!valid) {
      return;
    }
    this.isInfoSaving = true;
    this.customer.save(this.customerDataCache)
      .subscribe((res: ResultVO<any>) => {
        if (!Objects.valid(res)) {
          return;
        }
        if (res.code !== 200) {
          return;
        }
      }, (error: HttpErrorResponse) => {
        this.message.error(error.message);
      }, () => {
        this.reload();
        this.isInfoSaving = false;
        this.isInfoEditing = false;
      });
  }

  cancelInfoEdit(): void {
    this.customerDataCache = null;
    this.isInfoEditing = false;
    for (const k in this.customerDataValidate) {
      this.customerDataValidate[ k ] = null;
    }
  }

  addSpecialPriceRow(): void {
    if (Objects.valid(this.editCache._id)) {
      this.message.warning('请先保存商品列表的更改!');
      return;
    }
    let item: CustomerSpecialPriceVO = {
      id: 0,
      productId: 0,
      productName: '',
      price: 0,
      priceType: 1
    };
    item[ '_id' ] = this.customerSpecialPriceCountIndex++;
    this.customerData.specialPrices = [
      item,
      ...this.customerData.specialPrices
    ];
    this.startEditSpecialPrice(item[ '_id' ], true);
  }

  onProductSearch(value: string): void {
    this.isProductsLoading = true;
    this.searchChange$.next(value);
  }

  onChangeSelectedProduct(event: TempProductVO): void {
    this.editCache.data.productId = event.id;
    this.editCache.data.productName = event.name;
  }

  startEditSpecialPrice(_id: number, isAdd?: boolean): void {
    if (Objects.valid(this.editCache._id)) {
      this.message.warning('请先保存特价列表的更改!');
      return;
    }
    const index = this.customerData.specialPrices.findIndex(item => item[ '_id' ] === _id);
    if (index === -1) {
      this.message.error('没有该特价条目!');
      return;
    }
    this.editCache._id = _id;
    this.editCache.data = {};
    const t: CustomerSpecialPriceVO = this.customerData.specialPrices[ index ];
    Object.assign(this.editCache.data, t);
    this.editCache.currentProduct = {
      id: t.productId,
      name: t.productName
    };
    this.editCache.product = this.editCache.currentProduct;
    if (Objects.valid(isAdd) && isAdd) {
      this.editCache.isAdd = isAdd;
    } else {
      this.editCache.isAdd = false;
    }
  }

  confirmSpecialPriceDelete(_id: number): void {
    this.customerData.specialPrices = this.customerData.specialPrices.filter(item => item[ '_id' ] !== _id);
  }

  cancelSpecialPriceEdit(_id: number): void {
    if (Objects.valid(this.editCache.isAdd) && this.editCache.isAdd) {
      this.customerData.specialPrices = this.customerData.specialPrices.filter(item => item[ '_id' ] !== _id);
    }
    Object.assign(this.editCache, this.defaultEditCache);
  }

  saveSpecialPriceEdit(_id: number): void {
    if (_id !== this.editCache._id) {
      return;
    }
    const index = this.customerData.specialPrices.findIndex(item => item[ '_id' ] === _id);
    Object.assign(this.customerData.specialPrices[ index ], this.editCache.data);
    Object.assign(this.editCache, this.defaultEditCache);
    // TODO: 提交远程
  }

  reload(): void {
    this.isLoading = true;
    Object.assign(this.editCache, this.defaultEditCache);
    this.customer.find(this.customerId)
      .subscribe((res: ResultVO<CustomerVO>) => {
        if (!Objects.valid(res)) {
          return;
        }
        this.isLoading = false;
        this.customerData = res.data;
        if (Objects.valid(this.customerData.specialPrices)) {
          this.customerData.specialPrices.forEach(item => {
            item[ '_id' ] = this.customerSpecialPriceCountIndex++;
          });
        }
      }, (error: HttpErrorResponse) => {
        this.message.error(error.message);
      });
  }

}

interface TempProductVO {

  id: number;
  name: string;

  [ key: string ]: any;

}

interface TempCustomerSpecialPriceVO {

  id?: number;
  productId?: number;
  productName?: string;
  price?: string;
  priceType?: number;

}