import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ActionResolution } from './action-resolution.model';
import { ActionResolutionPopupService } from './action-resolution-popup.service';
import { ActionResolutionService } from './action-resolution.service';
import { OrderResolution, OrderResolutionService } from '../order-resolution';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-action-resolution-dialog',
    templateUrl: './action-resolution-dialog.component.html'
})
export class ActionResolutionDialogComponent implements OnInit {

    actionResolution: ActionResolution;
    isSaving: boolean;

    orderresolutions: OrderResolution[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private actionResolutionService: ActionResolutionService,
        private orderResolutionService: OrderResolutionService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.orderResolutionService.query()
            .subscribe((res: ResponseWrapper) => { this.orderresolutions = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.actionResolution.id !== undefined) {
            this.subscribeToSaveResponse(
                this.actionResolutionService.update(this.actionResolution));
        } else {
            this.subscribeToSaveResponse(
                this.actionResolutionService.create(this.actionResolution));
        }
    }

    private subscribeToSaveResponse(result: Observable<ActionResolution>) {
        result.subscribe((res: ActionResolution) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ActionResolution) {
        this.eventManager.broadcast({ name: 'actionResolutionListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackOrderResolutionById(index: number, item: OrderResolution) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-action-resolution-popup',
    template: ''
})
export class ActionResolutionPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private actionResolutionPopupService: ActionResolutionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.actionResolutionPopupService
                    .open(ActionResolutionDialogComponent as Component, params['id']);
            } else {
                this.actionResolutionPopupService
                    .open(ActionResolutionDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
