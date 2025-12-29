import { NgModule } from '@angular/core';
import { RouterModule, Routes } from "@angular/router";
import { CategoriesComponent } from "./modules/categories/categories.component";
import { ListDeviceComponent } from "./modules/list-appareil/list-device.component";

const routes: Routes = [
  { path: 'categories', component: CategoriesComponent },
  { path: 'devices', component: ListDeviceComponent },
  { path: '', redirectTo: 'devices', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
