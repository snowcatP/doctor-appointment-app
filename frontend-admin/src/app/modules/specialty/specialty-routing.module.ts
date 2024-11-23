import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SpecialtyTableComponent } from './specialty-table/specialty-table.component';
import { SpecialtyAddNewComponent } from './specialty-add-new/specialty-add-new.component';
import { SpecialtyEditComponent } from './specialty-edit/specialty-edit.component';

const routes: Routes = [
  {
    path: '',
    component: SpecialtyTableComponent
  },
  {
    path: 'add-specialty',
    component: SpecialtyAddNewComponent
  },
  {
    path: 'edit-specialty',
    component: SpecialtyEditComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SpecialtyRoutingModule { }
