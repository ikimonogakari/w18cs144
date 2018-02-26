import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { catchError } from 'rxjs/operators';

@Injectable()
export class BlogService {
  private posts: Post[];
  private maxId: number;

  constructor() {
    this.fetchPosts();
  }

  fetchPosts(): void {
    this.posts = <Post[]> JSON.parse(localStorage.getItem('postList'));
    if (this.posts == null || this.posts.length === 0) {
      this.posts = [];
      this.maxId = 1;
    } else {
      this.maxId = this.posts[this.posts.length - 1].postid + 1;
    }
  };

  getPosts(): Post[] {
    return this.posts;
  }

  getPost(id: number): Post {
    for (let i of this.posts) {
      if (i.postid === id) {
        return i;
      }
    }
    return null;
  }

  newPost(): Post {
    // only need to send username, postid, title and body
    let newPost: Post = new Post();
    newPost.postid = this.maxId;
    newPost.title = '';
    newPost.body = '';
    newPost.created = new Date(Date.now());
    newPost.modified = new Date(Date.now());
    this.posts.push(newPost);
    this.maxId++;
    return newPost;
  }

  updatePost(post: Post): void {
    // only need to send title and body
    post.modified = new Date(Date.now());
    for (let i in this.posts) {
      if (this.posts[i].postid === post.postid) {
        this.posts[i] = post;
        break;
      }
    }
    localStorage.setItem('postList', JSON.stringify(this.posts));
  }

  deletePost(id: number): void {
    for (let i in this.posts) {
      if (this.posts[i].postid === id) {
        this.posts.splice(Number(i), 1);
        break;
      }
    }
    localStorage.setItem('postList', JSON.stringify(this.posts));
  }

}

export class Post {
  postid: number;
  created: Date;
  modified: Date;
  title: string;
  body: string;
}
