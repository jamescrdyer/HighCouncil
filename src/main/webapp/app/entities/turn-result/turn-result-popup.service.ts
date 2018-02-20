import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TurnResult } from './turn-result.model';
import { TurnResultService } from './turn-result.service';
import { TurnResultDialogComponent } from './turn-result-dialog.component';

@Injectable()
export class TurnResultPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private turnResultService: TurnResultService

    ) {
        this.ngbModalRef = null;
    }

    show(turnResult: TurnResult): NgbModalRef {
        this.ngbModalRef = this.turnResultModalRef(TurnResultDialogComponent as Component, turnResult);
        return this.ngbModalRef;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.turnResultService.find(id).subscribe((turnResult) => {
                    this.ngbModalRef = this.turnResultModalRef(component, turnResult);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.turnResultModalRef(component, new TurnResult());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    turnResultModalRef(component: Component, turnResult: TurnResult): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.turnResult = turnResult;
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
