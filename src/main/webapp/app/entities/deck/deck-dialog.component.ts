import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Deck } from './deck.model';
import { DeckPopupService } from './deck-popup.service';
import { DeckService } from './deck.service';
import { Game, GameService } from '../game';
import { Card, CardService } from '../card';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-deck-dialog',
    templateUrl: './deck-dialog.component.html'
})
export class DeckDialogComponent implements OnInit {

    deck: Deck;
    isSaving: boolean;

    games: Game[];

    cards: Card[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private deckService: DeckService,
        private gameService: GameService,
        private cardService: CardService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.gameService.query()
            .subscribe((res: ResponseWrapper) => { this.games = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.cardService.query()
            .subscribe((res: ResponseWrapper) => { this.cards = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.deck.id !== undefined) {
            this.subscribeToSaveResponse(
                this.deckService.update(this.deck));
        } else {
            this.subscribeToSaveResponse(
                this.deckService.create(this.deck));
        }
    }

    private subscribeToSaveResponse(result: Observable<Deck>) {
        result.subscribe((res: Deck) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Deck) {
        this.eventManager.broadcast({ name: 'deckListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackGameById(index: number, item: Game) {
        return item.id;
    }

    trackCardById(index: number, item: Card) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-deck-popup',
    template: ''
})
export class DeckPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private deckPopupService: DeckPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.deckPopupService
                    .open(DeckDialogComponent as Component, params['id']);
            } else {
                this.deckPopupService
                    .open(DeckDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
