import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISentence } from 'app/shared/model/sentence.model';
import { IFilters } from 'app/shared/model/filters.model';

type EntityArrayResponseType = HttpResponse<ISentence[]>;
type EntityFilterResponseType = HttpResponse<IFilters>;
type EntityResponseType = HttpResponse<ISentence>;

@Injectable({providedIn: 'root'})
export class SearchSystemService {
    private resourceUrl = SERVER_API_URL + 'api/_search/sentences';
    private filters = SERVER_API_URL + 'api/sentences/filters';

    constructor(private http: HttpClient) {}

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISentence[]>(this.resourceUrl, {params: options, observe: 'response'});
    }

    getFilters(): Observable<EntityFilterResponseType> {
        return this.http.get<IFilters>(this.filters, {observe: 'response'});
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISentence[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISentence>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
