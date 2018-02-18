import { Component, OnInit } from '@angular/core';
import { Post, BlogService } from '../blog.service';
import { ActivatedRoute, ParamMap, Router, NavigationEnd } from '@angular/router';
import { Observable } from 'rxjs/Observable';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})

export class ListComponent implements OnInit {
  posts: Post[];
  selectedPost: Post;
  selectedId: number;

  constructor(
    private bs: BlogService,
    private aRoute: ActivatedRoute,
    private router: Router) {
    this.posts = this.bs.getPosts();
  }

  ngOnInit() {
    this.router.events.subscribe(
      event => {
        if (event instanceof NavigationEnd) {
          let child_route = this.aRoute.children[0].snapshot;
          this.selectedId = Number(child_route.params['id']);
        }
    });
  }

  newPost(): void {
    let t_post: Post = this.bs.newPost();
    this.selectedPost = t_post;
    this.router.navigate(['edit', this.selectedPost.postid]);
  }

  onSelect(p: Post): void {
    this.selectedPost = p;
    this.router.navigate(['edit', this.selectedPost.postid]);
  }

  updateClass(p: Post) {
    if (p.postid === this.selectedId) {
      return 'list-group-item d-flex justify-content-between align-items-center list-group-item-action active';
    } else {
      return 'list-group-item d-flex justify-content-between align-items-center list-group-item-action';
    }
  }
}
