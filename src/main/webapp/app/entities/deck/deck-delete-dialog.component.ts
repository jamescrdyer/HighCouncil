import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Deck } from './deck.model';
import { DeckPopupService } from './deck-popup.service';
import { DeckService } from './deck.service';

@Component({
    selector: 'jhi-deck-delete-dialog',
    templateUrl: './deck-delete-dialog.component.html'
})
export class DeckDeleteDialogComponent {

    deck: Deck;

    constructor(
        private deckService: DeckService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.deckService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'deckListModification',
                content: 'Deleted an deck'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-deck-delete-popup',
    template: ''
})
export class DeckDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private deckPopupService: DeckPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.deckPopupService
                .open(DeckDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
