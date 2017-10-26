import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { OrderResolution } from './order-resolution.model';
import { OrderResolutionPopupService } from './order-resolution-popup.service';
import { OrderResolutionService } from './order-resolution.service';

@Component({
    selector: 'jhi-order-resolution-delete-dialog',
    templateUrl: './order-resolution-delete-dialog.component.html'
})
export class OrderResolutionDeleteDialogComponent {

    orderResolution: OrderResolution;

    constructor(
        private orderResolutionService: OrderResolutionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.orderResolutionService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'orderResolutionListModification',
                content: 'Deleted an orderResolution'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-order-resolution-delete-popup',
    template: ''
})
export class OrderResolutionDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private orderResolutionPopupService: OrderResolutionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.orderResolutionPopupService
                .open(OrderResolutionDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
