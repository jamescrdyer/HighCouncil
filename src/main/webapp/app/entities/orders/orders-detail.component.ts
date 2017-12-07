import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Orders } from './orders.model';
import { OrdersService } from './orders.service';

@Component({
    selector: 'jhi-orders-detail',
    templateUrl: './orders-detail.component.html'
})
export class OrdersDetailComponent implements OnInit, OnDestroy {

    orders: Orders;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private ordersService: OrdersService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInOrders();
    }

    load(id) {
        this.ordersService.find(id).subscribe((orders) => {
            this.orders = orders;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInOrders() {
        this.eventSubscriber = this.eventManager.subscribe(
            'ordersListModification',
            (response) => this.load(this.orders.id)
        );
    }
}
