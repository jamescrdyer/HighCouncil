import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Kingdom } from './kingdom.model';
import { KingdomPopupService } from './kingdom-popup.service';
import { KingdomService } from './kingdom.service';

@Component({
    selector: 'jhi-kingdom-delete-dialog',
    templateUrl: './kingdom-delete-dialog.component.html'
})
export class KingdomDeleteDialogComponent {

    kingdom: Kingdom;

    constructor(
        private kingdomService: KingdomService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.kingdomService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'kingdomListModification',
                content: 'Deleted an kingdom'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-kingdom-delete-popup',
    template: ''
})
export class KingdomDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private kingdomPopupService: KingdomPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.kingdomPopupService
                .open(KingdomDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
