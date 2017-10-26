import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { OrderResolution } from './order-resolution.model';
import { OrderResolutionPopupService } from './order-resolution-popup.service';
import { OrderResolutionService } from './order-resolution.service';

@Component({
    selector: 'jhi-order-resolution-dialog',
    templateUrl: './order-resolution-dialog.component.html'
})
export class OrderResolutionDialogComponent implements OnInit {

    orderResolution: OrderResolution;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private orderResolutionService: OrderResolutionService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.orderResolution.id !== undefined) {
            this.subscribeToSaveResponse(
                this.orderResolutionService.update(this.orderResolution));
        } else {
            this.subscribeToSaveResponse(
                this.orderResolutionService.create(this.orderResolution));
        }
    }

    private subscribeToSaveResponse(result: Observable<OrderResolution>) {
        result.subscribe((res: OrderResolution) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: OrderResolution) {
        this.eventManager.broadcast({ name: 'orderResolutionListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-order-resolution-popup',
    template: ''
})
export class OrderResolutionPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private orderResolutionPopupService: OrderResolutionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.orderResolutionPopupService
                    .open(OrderResolutionDialogComponent as Component, params['id']);
            } else {
                this.orderResolutionPopupService
                    .open(OrderResolutionDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
