import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Kingdom } from './kingdom.model';
import { KingdomPopupService } from './kingdom-popup.service';
import { KingdomService } from './kingdom.service';
import { Game, GameService } from '../game';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-kingdom-dialog',
    templateUrl: './kingdom-dialog.component.html'
})
export class KingdomDialogComponent implements OnInit {

    kingdom: Kingdom;
    isSaving: boolean;

    games: Game[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private kingdomService: KingdomService,
        private gameService: GameService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.gameService.query()
            .subscribe((res: ResponseWrapper) => { this.games = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.kingdom.id !== undefined) {
            this.subscribeToSaveResponse(
                this.kingdomService.update(this.kingdom));
        } else {
            this.subscribeToSaveResponse(
                this.kingdomService.create(this.kingdom));
        }
    }

    private subscribeToSaveResponse(result: Observable<Kingdom>) {
        result.subscribe((res: Kingdom) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Kingdom) {
        this.eventManager.broadcast({ name: 'kingdomListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-kingdom-popup',
    template: ''
})
export class KingdomPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private kingdomPopupService: KingdomPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.kingdomPopupService
                    .open(KingdomDialogComponent as Component, params['id']);
            } else {
                this.kingdomPopupService
                    .open(KingdomDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
