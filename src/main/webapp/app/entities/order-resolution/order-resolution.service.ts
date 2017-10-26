import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { OrderResolution } from './order-resolution.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class OrderResolutionService {

    private resourceUrl = SERVER_API_URL + 'api/order-resolutions';

    constructor(private http: Http) { }

    create(orderResolution: OrderResolution): Observable<OrderResolution> {
        const copy = this.convert(orderResolution);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(orderResolution: OrderResolution): Observable<OrderResolution> {
        const copy = this.convert(orderResolution);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<OrderResolution> {
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
     * Convert a returned JSON object to OrderResolution.
     */
    private convertItemFromServer(json: any): OrderResolution {
        const entity: OrderResolution = Object.assign(new OrderResolution(), json);
        return entity;
    }

    /**
     * Convert a OrderResolution to a JSON which can be sent to the server.
     */
    private convert(orderResolution: OrderResolution): OrderResolution {
        const copy: OrderResolution = Object.assign({}, orderResolution);
        return copy;
    }
}
