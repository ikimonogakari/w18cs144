import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { EditComponent } from './edit/edit.component'
import { PreviewComponent } from './preview/preview.component'
import { NotFoundComponent } from './not-found/not-found.component'

const routes: Routes = [
  { path: 'edit/:id', component: EditComponent },
  { path: 'preview/:id', component: PreviewComponent},
  { path: '', children: []},
  { path: '**', component: NotFoundComponent }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {useHash: true})
  ],
  exports:[RouterModule]
})
export class AppRoutingModule { }
