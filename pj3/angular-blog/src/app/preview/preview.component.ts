import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { Parser, HtmlRenderer } from 'commonmark';
import { Post, BlogService } from '../blog.service'

@Component({
  selector: 'app-preview',
  templateUrl: './preview.component.html',
  styleUrls: ['./preview.component.css']
})
export class PreviewComponent implements OnInit {

  private post: Post = null;
  private sub: any;
  private title : string;
  private body : string;

  constructor(
    private bs: BlogService,
    private router: Router,
    private aRoute: ActivatedRoute
  ) { }

  ngOnInit() {
    this.sub = this.aRoute.params.subscribe(
      params => {
        let id : number = Number(params['id']);
        this.post = this.bs.getPost(id);
        this.renderHTML();
    });
  }

  renderHTML(): void {
    let reader = new Parser();
    let writer = new HtmlRenderer();
    this.title = writer.render(reader.parse(this.post.title));
    this.body = writer.render(reader.parse(this.post.body));
  }

  back():void {
    this.router.navigate(['edit', this.post.postid]);
  }
}
