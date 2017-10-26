import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { OrderResolution } from './order-resolution.model';
import { OrderResolutionService } from './order-resolution.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-order-resolution',
    templateUrl: './order-resolution.component.html'
})
export class OrderResolutionComponent implements OnInit, OnDestroy {
orderResolutions: OrderResolution[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private orderResolutionService: OrderResolutionService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.orderResolutionService.query().subscribe(
            (res: ResponseWrapper) => {
                this.orderResolutions = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInOrderResolutions();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: OrderResolution) {
        return item.id;
    }
    registerChangeInOrderResolutions() {
        this.eventSubscriber = this.eventManager.subscribe('orderResolutionListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
