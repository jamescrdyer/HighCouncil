import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Deck } from './deck.model';
import { DeckService } from './deck.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-deck',
    templateUrl: './deck.component.html'
})
export class DeckComponent implements OnInit, OnDestroy {
decks: Deck[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private deckService: DeckService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.deckService.query().subscribe(
            (res: ResponseWrapper) => {
                this.decks = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInDecks();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Deck) {
        return item.id;
    }
    registerChangeInDecks() {
        this.eventSubscriber = this.eventManager.subscribe('deckListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
