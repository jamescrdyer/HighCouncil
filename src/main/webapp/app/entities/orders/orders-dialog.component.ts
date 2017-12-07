import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Orders } from './orders.model';
import { OrdersPopupService } from './orders-popup.service';
import { OrdersService } from './orders.service';
import { Game, GameService } from '../game';
import { Player, PlayerService } from '../player';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-orders-dialog',
    templateUrl: './orders-dialog.component.html'
})
export class OrdersDialogComponent implements OnInit {

    orders: Orders;
    isSaving: boolean;

    games: Game[];

    players: Player[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private ordersService: OrdersService,
        private gameService: GameService,
        private playerService: PlayerService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.gameService.query()
            .subscribe((res: ResponseWrapper) => { this.games = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.playerService.query()
            .subscribe((res: ResponseWrapper) => { this.players = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.orders.id !== undefined) {
            this.subscribeToSaveResponse(
                this.ordersService.update(this.orders));
        } else {
            this.subscribeToSaveResponse(
                this.ordersService.create(this.orders));
        }
    }

    private subscribeToSaveResponse(result: Observable<Orders>) {
        result.subscribe((res: Orders) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Orders) {
        this.eventManager.broadcast({ name: 'ordersListModification', content: 'OK'});
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

    trackPlayerById(index: number, item: Player) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-orders-popup',
    template: ''
})
export class OrdersPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ordersPopupService: OrdersPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.ordersPopupService
                    .open(OrdersDialogComponent as Component, params['id']);
            } else {
                this.ordersPopupService
                    .open(OrdersDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
