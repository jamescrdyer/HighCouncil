import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { TurnResult } from './turn-result.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TurnResultService {

    private resourceUrl = SERVER_API_URL + 'api/turn-results';

    constructor(private http: Http) { }

    create(turnResult: TurnResult): Observable<TurnResult> {
        const copy = this.convert(turnResult);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(turnResult: TurnResult): Observable<TurnResult> {
        const copy = this.convert(turnResult);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<TurnResult> {
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
     * Convert a returned JSON object to TurnResult.
     */
    private convertItemFromServer(json: any): TurnResult {
        const entity: TurnResult = Object.assign(new TurnResult(), json);
        return entity;
    }

    /**
     * Convert a TurnResult to a JSON which can be sent to the server.
     */
    private convert(turnResult: TurnResult): TurnResult {
        const copy: TurnResult = Object.assign({}, turnResult);
        return copy;
    }
}
