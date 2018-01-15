import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { ExpectedOrderNumbers } from './expected-order-numbers.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ExpectedOrderNumbersService {

    private resourceUrl = SERVER_API_URL + 'api/expected-order-numbers';

    constructor(private http: Http) { }

    create(expectedOrderNumbers: ExpectedOrderNumbers): Observable<ExpectedOrderNumbers> {
        const copy = this.convert(expectedOrderNumbers);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(expectedOrderNumbers: ExpectedOrderNumbers): Observable<ExpectedOrderNumbers> {
        const copy = this.convert(expectedOrderNumbers);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<ExpectedOrderNumbers> {
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

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to ExpectedOrderNumbers.
     */
    private convertItemFromServer(json: any): ExpectedOrderNumbers {
        const entity: ExpectedOrderNumbers = Object.assign(new ExpectedOrderNumbers(), json);
        return entity;
    }

    /**
     * Convert a ExpectedOrderNumbers to a JSON which can be sent to the server.
     */
    private convert(expectedOrderNumbers: ExpectedOrderNumbers): ExpectedOrderNumbers {
        const copy: ExpectedOrderNumbers = Object.assign({}, expectedOrderNumbers);
        return copy;
    }
}
