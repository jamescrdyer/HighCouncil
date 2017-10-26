import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Deck } from './deck.model';
import { DeckService } from './deck.service';

@Component({
    selector: 'jhi-deck-detail',
    templateUrl: './deck-detail.component.html'
})
export class DeckDetailComponent implements OnInit, OnDestroy {

    deck: Deck;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private deckService: DeckService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDecks();
    }

    load(id) {
        this.deckService.find(id).subscribe((deck) => {
            this.deck = deck;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDecks() {
        this.eventSubscriber = this.eventManager.subscribe(
            'deckListModification',
            (response) => this.load(this.deck.id)
        );
    }
}
