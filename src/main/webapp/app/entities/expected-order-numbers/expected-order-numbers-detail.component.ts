import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { ExpectedOrderNumbers } from './expected-order-numbers.model';
import { ExpectedOrderNumbersService } from './expected-order-numbers.service';

@Component({
    selector: 'jhi-expected-order-numbers-detail',
    templateUrl: './expected-order-numbers-detail.component.html'
})
export class ExpectedOrderNumbersDetailComponent implements OnInit, OnDestroy {

    expectedOrderNumbers: ExpectedOrderNumbers;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private expectedOrderNumbersService: ExpectedOrderNumbersService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInExpectedOrderNumbers();
    }

    load(id) {
        this.expectedOrderNumbersService.find(id).subscribe((expectedOrderNumbers) => {
            this.expectedOrderNumbers = expectedOrderNumbers;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInExpectedOrderNumbers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'expectedOrderNumbersListModification',
            (response) => this.load(this.expectedOrderNumbers.id)
        );
    }
}
