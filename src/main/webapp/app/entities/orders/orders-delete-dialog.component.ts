import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Orders } from './orders.model';
import { OrdersPopupService } from './orders-popup.service';
import { OrdersService } from './orders.service';

@Component({
    selector: 'jhi-orders-delete-dialog',
    templateUrl: './orders-delete-dialog.component.html'
})
export class OrdersDeleteDialogComponent {

    orders: Orders;

    constructor(
        private ordersService: OrdersService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.ordersService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'ordersListModification',
                content: 'Deleted an orders'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-orders-delete-popup',
    template: ''
})
export class OrdersDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ordersPopupService: OrdersPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.ordersPopupService
                .open(OrdersDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
