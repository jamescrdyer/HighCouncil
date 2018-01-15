import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ExpectedOrderNumbers } from './expected-order-numbers.model';
import { ExpectedOrderNumbersService } from './expected-order-numbers.service';

@Injectable()
export class ExpectedOrderNumbersPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private expectedOrderNumbersService: ExpectedOrderNumbersService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.expectedOrderNumbersService.find(id).subscribe((expectedOrderNumbers) => {
                    this.ngbModalRef = this.expectedOrderNumbersModalRef(component, expectedOrderNumbers);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.expectedOrderNumbersModalRef(component, new ExpectedOrderNumbers());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    expectedOrderNumbersModalRef(component: Component, expectedOrderNumbers: ExpectedOrderNumbers): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.expectedOrderNumbers = expectedOrderNumbers;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
