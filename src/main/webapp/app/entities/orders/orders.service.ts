import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Orders } from './orders.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class OrdersService {

    private resourceUrl = SERVER_API_URL + 'api/orders';

    constructor(private http: Http) { }

    create(orders: Orders): Observable<Orders> {
        const copy = this.convert(orders);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(orders: Orders): Observable<Orders> {
        const copy = this.convert(orders);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Orders> {
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
     * Convert a returned JSON object to Orders.
     */
    private convertItemFromServer(json: any): Orders {
        const entity: Orders = Object.assign(new Orders(), json);
        return entity;
    }

    /**
     * Convert a Orders to a JSON which can be sent to the server.
     */
    private convert(orders: Orders): Orders {
        const copy: Orders = Object.assign({}, orders);
        return copy;
    }
}
