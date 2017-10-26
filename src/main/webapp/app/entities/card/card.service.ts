import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Card } from './card.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CardService {

    private resourceUrl = SERVER_API_URL + 'api/cards';

    constructor(private http: Http) { }

    create(card: Card): Observable<Card> {
        const copy = this.convert(card);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(card: Card): Observable<Card> {
        const copy = this.convert(card);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Card> {
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
     * Convert a returned JSON object to Card.
     */
    private convertItemFromServer(json: any): Card {
        const entity: Card = Object.assign(new Card(), json);
        return entity;
    }

    /**
     * Convert a Card to a JSON which can be sent to the server.
     */
    private convert(card: Card): Card {
        const copy: Card = Object.assign({}, card);
        return copy;
    }
}
