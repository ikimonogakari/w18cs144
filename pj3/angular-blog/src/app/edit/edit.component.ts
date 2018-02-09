import { Component, OnInit, Input } from '@angular/core';
import { Post, BlogService } from '../blog.service'

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css'],
  providers: [BlogService]
})
export class EditComponent implements OnInit {
  @Input() post: Post;

  constructor(
    private bs: BlogService) {}
    // private router: Router,
    // private activatedRoute: ActivatedRoute

  ngOnInit() {
  }

  save(): void {
    this.bs.updatePost(this.post);
  }

  delete(): void {
    this.bs.deletePosts(this.post.postid);
  }

  preview(): void {
  }
}
