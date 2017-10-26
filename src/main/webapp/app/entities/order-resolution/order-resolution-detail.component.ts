import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { OrderResolution } from './order-resolution.model';
import { OrderResolutionService } from './order-resolution.service';

@Component({
    selector: 'jhi-order-resolution-detail',
    templateUrl: './order-resolution-detail.component.html'
})
export class OrderResolutionDetailComponent implements OnInit, OnDestroy {

    orderResolution: OrderResolution;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private orderResolutionService: OrderResolutionService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInOrderResolutions();
    }

    load(id) {
        this.orderResolutionService.find(id).subscribe((orderResolution) => {
            this.orderResolution = orderResolution;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInOrderResolutions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'orderResolutionListModification',
            (response) => this.load(this.orderResolution.id)
        );
    }
}
