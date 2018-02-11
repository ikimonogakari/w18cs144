import { Component, OnInit } from '@angular/core';
import { Post, BlogService } from '../blog.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css'],
  providers: [BlogService]
})
export class ListComponent implements OnInit {
  posts: Post[];
  selectedPost: Post;

  constructor(private bs: BlogService) {
    this.posts = this.bs.getPosts();
  }

  ngOnInit() {
  }

  newPost(): void {
    let t_post: Post = this.bs.newPost();
    this.selectedPost = t_post;
  }

  onSelect(p: Post): void {
    this.selectedPost = p;
  }

}
