import { NgModule } from "@angular/core";
import { CoreModule } from "../../core/core.module";
import { SharedModule } from "../../shared/shared.module";
import { WorkspaceComponent } from "./workspace.component";
import { RouteReuseStrategy, RouterModule, Routes } from "@angular/router";
import { SimpleReuseStrategy } from "../../core/strategy/simple-reuse.strategy";
import { TabComponent } from "./tab/tab.component";
import { AuthorizationGuard } from "../../guards/authorization.guard";

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
  {
    path: 'dashboard',
    loadChildren: './dashboard/dashboard.module#DashboardModule',
    canActivate: [ AuthorizationGuard ],
    data: {
      title: '控制台',
      removable: true
    }
  }, {
    path: 'purchase-order',
    loadChildren: './purchase-order/purchase-order.module#PurchaseOrderModule'
  }, {
    path: 'user-center',
    loadChildren: './user-center/user-center.module#UserCenterModule'
  }
];

@NgModule({
  providers: [
    { provide: RouteReuseStrategy, useClass: SimpleReuseStrategy }
  ],
  imports: [ CoreModule, SharedModule, RouterModule.forChild(routes) ],
  declarations: [ WorkspaceComponent, TabComponent ],
  exports: [ WorkspaceComponent, RouterModule ]
})
export class WorkspaceModule {
}
