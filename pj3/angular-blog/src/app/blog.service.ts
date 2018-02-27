import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { catchError } from 'rxjs/operators';
import { JwtHelper } from 'angular2-jwt';
import { CookieService } from 'angular2-cookie/core';

@Injectable()
export class BlogService {
  private posts: Post[] = [];
  private maxId: number = 0;
  private username : String;

  private url : String = 'http://192.168.1.102:3000/api';

  constructor(
    private cookieService: CookieService,
    private http: HttpClient) {
    let jwt_decode = new JwtHelper();
    let payload = jwt_decode.decodeToken(this.cookieService.get('jwt'));
    console.log(payload);
    this.username = payload['usr'];
    this.fetchPosts();
  }

  fetchPosts(): void {
    let getUrl = this.url + '/' + this.username;
    console.log(getUrl);
    this.http.get(getUrl,
    {
      headers: new HttpHeaders().set('Content-Type', 'application/json'),
      responseType: 'json' 
   }).subscribe(
      data => {
        for (let i in data) {
          let j = data[i];
          let p = new Post();
          p.postid = j['postid'];
          p.created = new Date(j['created']);
          p.modified = new Date(j['modified']);
          p.title = j['title'];
          p.body = j['body'];
          this.posts.push(p);
          this.maxId = Math.max(this.maxId, p.postid);
        }
        this.maxId += 1;
      });
    /*
    this.posts = <Post[]> JSON.parse(localStorage.getItem('postList'));
    if (this.posts == null || this.posts.length === 0) {
      this.posts = [];
      this.maxId = 1;
    } else {
      this.maxId = this.posts[this.posts.length - 1].postid + 1;
    } */
  };

  getPosts(): Post[] {
    return this.posts;
  }
  // does it need to implement using http request??
  getPost(id: number): Post {
    for (let i of this.posts) {
      if (i.postid === id) {
        return i;
      }
    }
    return null;
  }

  newPost(): any {
    // only need to send username, postid, title and body
    let newPost: Post = new Post();
    newPost.postid = this.maxId;
    newPost.title = '';
    newPost.body = '';
    newPost.created = new Date(Date.now());
    newPost.modified = new Date(Date.now());
    let postUrl = this.url + '/' + this.username + '/' + newPost.postid;
    let postBody = {};
    postBody['username'] = this.username;
    postBody['postid'] = newPost.postid;
    postBody['title'] = newPost.title;
    postBody['body'] = newPost.body;
    this.http.post(postUrl, postBody,
      {
        headers: new HttpHeaders().set('Content-Type', 'application/json'),
        responseType: 'text' 
     }).subscribe(data => {
      console.log(data);
      this.maxId++;
    });
    this.posts.push(newPost); // !!!
    return newPost; // it is possible that this post didn't created successfully
  }

  updatePost(post: Post): void {
    // only need to send title and body
    let putUrl = this.url + '/' + this.username + '/' + post.postid;
    let putBody = {};
    putBody['title'] = post.title;
    putBody['body'] = post.body;
    post.modified = new Date(Date.now());
    this.http.put(putUrl, putBody,
      {
        headers: new HttpHeaders().set('Content-Type', 'application/json'),
        responseType: 'text' 
     }).subscribe(data => {
      console.log(data);
    });
    for (let i in this.posts) {
      if (this.posts[i].postid === post.postid) {
        this.posts[i] = post;
        break;
      }
    }
    // localStorage.setItem('postList', JSON.stringify(this.posts));
  }

  deletePost(id: number): void {
    let deleteUrl = this.url + '/' + this.username + '/' + id;
    this.http.delete(deleteUrl,
      {
        headers: new HttpHeaders().set('Content-Type', 'application/json'),
        responseType: 'text' 
     }).subscribe(data => {
      console.log(data);
    });
    for (let i in this.posts) {
      if (this.posts[i].postid === id) {
        this.posts.splice(Number(i), 1);
        break;
      }
    }
    // localStorage.setItem('postList', JSON.stringify(this.posts));
  }

}

export class Post {
  postid: number;
  created: Date;
  modified: Date;
  title: string;
  body: string;
}
