import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISentence } from 'app/shared/model/sentence.model';
import { SentenceService } from './sentence.service';

@Component({
    selector: 'jhi-sentence-delete-dialog',
    templateUrl: './sentence-delete-dialog.component.html'
})
export class SentenceDeleteDialogComponent {
    sentence: ISentence;

    constructor(private sentenceService: SentenceService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.sentenceService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'sentenceListModification',
                content: 'Deleted an sentence'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sentence-delete-popup',
    template: ''
})
export class SentenceDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sentence }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SentenceDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.sentence = sentence;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
