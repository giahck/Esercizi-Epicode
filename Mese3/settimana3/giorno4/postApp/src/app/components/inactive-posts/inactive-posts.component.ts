import { Component, OnInit } from '@angular/core';
import { Post } from 'src/app/models/post.interface';
import { PostService } from 'src/app/service/post.service';

@Component({
    selector: 'app-inactive-posts',
    templateUrl: './inactive-posts.component.html',
    styleUrls: ['./inactive-posts.component.scss'],
})
export class InactivePostsComponent {
    posts!: Post[];

    constructor(private postSrv: PostService) {}

    ngOnInit() {
        console.log('ngOnInit attivato');
        this.postSrv.getPosts().subscribe((data) => {
            this.posts = data;
        });
    }

    enablePost(id: number, index: number) {
        this.postSrv.updatePost(id, { completed: true }).subscribe();
        this.posts.splice(index, 1);
    }
}
