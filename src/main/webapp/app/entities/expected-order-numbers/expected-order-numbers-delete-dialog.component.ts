import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ExpectedOrderNumbers } from './expected-order-numbers.model';
import { ExpectedOrderNumbersPopupService } from './expected-order-numbers-popup.service';
import { ExpectedOrderNumbersService } from './expected-order-numbers.service';

@Component({
    selector: 'jhi-expected-order-numbers-delete-dialog',
    templateUrl: './expected-order-numbers-delete-dialog.component.html'
})
export class ExpectedOrderNumbersDeleteDialogComponent {

    expectedOrderNumbers: ExpectedOrderNumbers;

    constructor(
        private expectedOrderNumbersService: ExpectedOrderNumbersService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.expectedOrderNumbersService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'expectedOrderNumbersListModification',
                content: 'Deleted an expectedOrderNumbers'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-expected-order-numbers-delete-popup',
    template: ''
})
export class ExpectedOrderNumbersDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private expectedOrderNumbersPopupService: ExpectedOrderNumbersPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.expectedOrderNumbersPopupService
                .open(ExpectedOrderNumbersDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
