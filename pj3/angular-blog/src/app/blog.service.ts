import { Injectable } from '@angular/core';

@Injectable()
export class BlogService {
  private posts: Post[];
  private maxId: number;

  constructor() {
    this.fetchPosts();
  }

  fetchPosts(): void {
    this.posts = JSON.parse(localStorage.getItem('postList'));
    if (this.posts == null || this.posts.length == 0) {
      this.posts = [];
      this.maxId = 1;
    } else {
      this.maxId = this.posts[this.posts.length-1].postid;
    }
    /* for test */
    if (this.posts == null || this.posts.length == 0) {
      let p1: Post = this.newPost();
      p1.title = 'title 1';
      p1.body = '**Body Title**';
      this.updatePost(p1);
      let p2: Post = this.newPost();
      p2.title = 'title 2';
      p2.body = '- item 0';
      this.updatePost(p2);
    }
  }

  getPosts(): Post[] {
    return this.posts;
  }

  getPost(id: number): Post {
    for (let i of this.posts) {
      if (i.postid == id) {
        return i;
      }
    }
  }

  newPost(): Post {
    let newPost: Post = new Post();
    newPost.postid = this.maxId;
    this.posts.push(newPost);
    this.maxId++;
    return newPost;
  }

  updatePost(post: Post): void {
    for (let i in this.posts) {
      if (this.posts[i].postid == post.postid) {
        this.posts[i] = post;
        break;
      }
    }
    localStorage.setItem('postList', JSON.stringify(this.posts));
  }

  deletePosts(postid: number): void {
    for (let i in this.posts) {
      if (this.posts[i].postid == postid) {
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