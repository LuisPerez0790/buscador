import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Sentence } from 'app/shared/model/sentence.model';
import { SearchSystemComponent } from './search-system.component';
import { ISentence } from 'app/shared/model/sentence.model';
import { SearchSystemService} from './search-system.service';

@Injectable({ providedIn: 'root' })
export class SearchSystemResolvePagingParams implements Resolve<any> {
    constructor(private service: SearchSystemService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((sentence: HttpResponse<Sentence>) => sentence.body));
        }
        return of(new Sentence());
    }
}

export const SearchSystemRoute: Routes = [
    {
        path: '',
        component: SearchSystemComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [],
            defaultSort: 'id,asc',
            pageTitle: 'sentencesgwApp.sentence.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
