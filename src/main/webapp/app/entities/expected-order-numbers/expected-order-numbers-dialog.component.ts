import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ExpectedOrderNumbers } from './expected-order-numbers.model';
import { ExpectedOrderNumbersPopupService } from './expected-order-numbers-popup.service';
import { ExpectedOrderNumbersService } from './expected-order-numbers.service';

@Component({
    selector: 'jhi-expected-order-numbers-dialog',
    templateUrl: './expected-order-numbers-dialog.component.html'
})
export class ExpectedOrderNumbersDialogComponent implements OnInit {

    expectedOrderNumbers: ExpectedOrderNumbers;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private expectedOrderNumbersService: ExpectedOrderNumbersService,
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
        if (this.expectedOrderNumbers.id !== undefined) {
            this.subscribeToSaveResponse(
                this.expectedOrderNumbersService.update(this.expectedOrderNumbers));
        } else {
            this.subscribeToSaveResponse(
                this.expectedOrderNumbersService.create(this.expectedOrderNumbers));
        }
    }

    private subscribeToSaveResponse(result: Observable<ExpectedOrderNumbers>) {
        result.subscribe((res: ExpectedOrderNumbers) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ExpectedOrderNumbers) {
        this.eventManager.broadcast({ name: 'expectedOrderNumbersListModification', content: 'OK'});
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
    selector: 'jhi-expected-order-numbers-popup',
    template: ''
})
export class ExpectedOrderNumbersPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private expectedOrderNumbersPopupService: ExpectedOrderNumbersPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.expectedOrderNumbersPopupService
                    .open(ExpectedOrderNumbersDialogComponent as Component, params['id']);
            } else {
                this.expectedOrderNumbersPopupService
                    .open(ExpectedOrderNumbersDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
