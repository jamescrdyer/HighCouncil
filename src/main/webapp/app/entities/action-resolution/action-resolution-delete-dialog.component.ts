import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ActionResolution } from './action-resolution.model';
import { ActionResolutionPopupService } from './action-resolution-popup.service';
import { ActionResolutionService } from './action-resolution.service';

@Component({
    selector: 'jhi-action-resolution-delete-dialog',
    templateUrl: './action-resolution-delete-dialog.component.html'
})
export class ActionResolutionDeleteDialogComponent {

    actionResolution: ActionResolution;

    constructor(
        private actionResolutionService: ActionResolutionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.actionResolutionService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'actionResolutionListModification',
                content: 'Deleted an actionResolution'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-action-resolution-delete-popup',
    template: ''
})
export class ActionResolutionDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private actionResolutionPopupService: ActionResolutionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.actionResolutionPopupService
                .open(ActionResolutionDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
