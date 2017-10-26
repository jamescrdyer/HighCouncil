import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { ActionResolution } from './action-resolution.model';
import { ActionResolutionService } from './action-resolution.service';

@Component({
    selector: 'jhi-action-resolution-detail',
    templateUrl: './action-resolution-detail.component.html'
})
export class ActionResolutionDetailComponent implements OnInit, OnDestroy {

    actionResolution: ActionResolution;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private actionResolutionService: ActionResolutionService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInActionResolutions();
    }

    load(id) {
        this.actionResolutionService.find(id).subscribe((actionResolution) => {
            this.actionResolution = actionResolution;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInActionResolutions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'actionResolutionListModification',
            (response) => this.load(this.actionResolution.id)
        );
    }
}
