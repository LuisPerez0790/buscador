import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ISentence } from 'app/shared/model/sentence.model';
import { SentenceService } from './sentence.service';

@Component({
    selector: 'jhi-sentence-update',
    templateUrl: './sentence-update.component.html'
})
export class SentenceUpdateComponent implements OnInit {
    private _sentence: ISentence;
    isSaving: boolean;

    constructor(private sentenceService: SentenceService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ sentence }) => {
            this.sentence = sentence;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.sentence.id !== undefined) {
            this.subscribeToSaveResponse(this.sentenceService.update(this.sentence));
        } else {
            this.subscribeToSaveResponse(this.sentenceService.create(this.sentence));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISentence>>) {
        result.subscribe((res: HttpResponse<ISentence>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get sentence() {
        return this._sentence;
    }

    set sentence(sentence: ISentence) {
        this._sentence = sentence;
    }
}
