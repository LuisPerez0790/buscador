import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISentence } from 'app/shared/model/sentence.model';

type EntityResponseType = HttpResponse<ISentence>;
type EntityArrayResponseType = HttpResponse<ISentence[]>;

@Injectable({ providedIn: 'root' })
export class SentenceService {
    private resourceUrl = SERVER_API_URL + 'api/sentences';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/sentences';

    constructor(private http: HttpClient) {}

    create(sentence: ISentence): Observable<EntityResponseType> {
        return this.http.post<ISentence>(this.resourceUrl, sentence, { observe: 'response' });
    }

    update(sentence: ISentence): Observable<EntityResponseType> {
        return this.http.put<ISentence>(this.resourceUrl, sentence, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISentence>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISentence[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISentence[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
