import { Component, OnInit } from '@angular/core';
import { Post, BlogService } from '../blog.service';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})

export class ListComponent implements OnInit {
  posts: Post[];
  selectedPost: Post;

  constructor(
    private bs: BlogService,
    private aRoute: ActivatedRoute,
    private router: Router) {
    this.posts = this.bs.getPosts();
  }

  ngOnInit() {
  }

  newPost(): void {
    let t_post: Post = this.bs.newPost();
    this.selectedPost = t_post;
    this.router.navigate(['/edit', this.selectedPost.postid]);
  }

  onSelect(p: Post): void {
    this.selectedPost = p;
    this.router.navigate(['/edit', this.selectedPost.postid]);
  }

}
