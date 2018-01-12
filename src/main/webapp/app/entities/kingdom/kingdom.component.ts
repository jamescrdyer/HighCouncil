import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Kingdom } from './kingdom.model';
import { KingdomService } from './kingdom.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-kingdom',
    templateUrl: './kingdom.component.html'
})
export class KingdomComponent implements OnInit, OnDestroy {
    kingdoms: Kingdom[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private kingdomService: KingdomService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.kingdomService.query().subscribe(
            (res: ResponseWrapper) => {
                this.kingdoms = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInKingdoms();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Kingdom) {
        return item.id;
    }
    registerChangeInKingdoms() {
        this.eventSubscriber = this.eventManager.subscribe('kingdomListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
