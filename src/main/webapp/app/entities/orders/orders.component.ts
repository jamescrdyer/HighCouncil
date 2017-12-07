import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Orders } from './orders.model';
import { OrdersService } from './orders.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-orders',
    templateUrl: './orders.component.html'
})
export class OrdersComponent implements OnInit, OnDestroy {
orders: Orders[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private ordersService: OrdersService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.ordersService.query().subscribe(
            (res: ResponseWrapper) => {
                this.orders = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInOrders();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Orders) {
        return item.id;
    }
    registerChangeInOrders() {
        this.eventSubscriber = this.eventManager.subscribe('ordersListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
