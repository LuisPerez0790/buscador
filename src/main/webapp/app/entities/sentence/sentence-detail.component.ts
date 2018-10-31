import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISentence } from 'app/shared/model/sentence.model';

@Component({
    selector: 'jhi-sentence-detail',
    templateUrl: './sentence-detail.component.html'
})
export class SentenceDetailComponent implements OnInit {
    sentence: ISentence;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sentence }) => {
            this.sentence = sentence;
        });
    }

    previousState() {
        window.history.back();
    }
}
