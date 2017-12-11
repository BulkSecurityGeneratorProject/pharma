import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Store } from './store.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class StoreService {

    private resourceUrl = SERVER_API_URL + 'api/stores';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/stores';

    constructor(private http: Http) { }

    create(store: Store): Observable<Store> {
        const copy = this.convert(store);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(store: Store): Observable<Store> {
        const copy = this.convert(store);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Store> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Store.
     */
    private convertItemFromServer(json: any): Store {
        const entity: Store = Object.assign(new Store(), json);
        return entity;
    }

    /**
     * Convert a Store to a JSON which can be sent to the server.
     */
    private convert(store: Store): Store {
        const copy: Store = Object.assign({}, store);
        return copy;
    }
}
