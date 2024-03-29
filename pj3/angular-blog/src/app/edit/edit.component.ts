import { Component, HostBinding, OnInit, Input, HostListener, style } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { slideInDownAnimation } from '../animations';

import { Post, BlogService } from '../blog.service';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css'],
  animations: [ slideInDownAnimation ]
})

export class EditComponent implements OnInit {
  @HostBinding('@routeAnimation') routeAnimation = true;
  @HostBinding('style.display')   display = 'block';
  // @HostBinding('style.position')  position = 'absolute';

  private post: Post = null;
  private sub: any;
  private editor = new FormGroup({
    title : new FormControl(),
    body : new FormControl()
  });

  constructor(
    private bs: BlogService,
    private aRoute: ActivatedRoute,
    private router: Router) {
  }

  ngOnInit() {
    this.sub = this.aRoute.params.subscribe(
      params => {
        let id : number = Number(params['id']);
        this.handleUnsaved();
        setTimeout(() => {
          this.post = this.bs.getPost(id);
          if (this.post == null) {
            this.router.navigateByUrl('error');
          }
        },500);
    });
  }

  save(): void {
    this.bs.updatePost(this.post);
    this.editor.markAsPristine();
  }

  delete(): void {
    this.bs.deletePost(this.post.postid);
    this.post = null;
    this.router.navigate(['']);
  }

  preview(): void {
    this.router.navigate(['preview', this.post.postid]);
  }

  ngOnDestroy() {
    this.handleUnsaved();
    this.sub.unsubscribe();
  }

  @HostListener('window:beforeunload')
  handleUnsaved() {
    if (this.post) {       // && !this.editor.pristine
      // console.log('unsaved modification');
      this.bs.updatePost(this.post);
    }
  }
}
