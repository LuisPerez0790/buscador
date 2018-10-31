import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Sentence } from 'app/shared/model/sentence.model';
import { SentenceService } from './sentence.service';
import { SentenceComponent } from './sentence.component';
import { SentenceDetailComponent } from './sentence-detail.component';
import { SentenceUpdateComponent } from './sentence-update.component';
import { SentenceDeletePopupComponent } from './sentence-delete-dialog.component';
import { ISentence } from 'app/shared/model/sentence.model';

@Injectable({ providedIn: 'root' })
export class SentenceResolve implements Resolve<ISentence> {
    constructor(private service: SentenceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((sentence: HttpResponse<Sentence>) => sentence.body));
        }
        return of(new Sentence());
    }
}

export const sentenceRoute: Routes = [
    {
        path: 'sentence',
        component: SentenceComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'sentencesgwApp.sentence.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sentence/:id/view',
        component: SentenceDetailComponent,
        resolve: {
            sentence: SentenceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sentencesgwApp.sentence.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sentence/new',
        component: SentenceUpdateComponent,
        resolve: {
            sentence: SentenceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sentencesgwApp.sentence.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sentence/:id/edit',
        component: SentenceUpdateComponent,
        resolve: {
            sentence: SentenceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sentencesgwApp.sentence.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const sentencePopupRoute: Routes = [
    {
        path: 'sentence/:id/delete',
        component: SentenceDeletePopupComponent,
        resolve: {
            sentence: SentenceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sentencesgwApp.sentence.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
