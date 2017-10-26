import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ActionResolution } from './action-resolution.model';
import { ActionResolutionService } from './action-resolution.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-action-resolution',
    templateUrl: './action-resolution.component.html'
})
export class ActionResolutionComponent implements OnInit, OnDestroy {
actionResolutions: ActionResolution[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private actionResolutionService: ActionResolutionService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.actionResolutionService.query().subscribe(
            (res: ResponseWrapper) => {
                this.actionResolutions = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInActionResolutions();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ActionResolution) {
        return item.id;
    }
    registerChangeInActionResolutions() {
        this.eventSubscriber = this.eventManager.subscribe('actionResolutionListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
