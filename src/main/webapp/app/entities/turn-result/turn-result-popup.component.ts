import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { TurnResult } from './turn-result.model';
import { TurnResultPopupService } from './turn-result-popup.service';
import { TurnResultDialogComponent } from './turn-result-dialog.component';

@Component({
    selector: 'jhi-turn-result-popup',
    template: ''
})
export class TurnResultPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private turnResultPopupService: TurnResultPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.turnResultPopupService.open(TurnResultDialogComponent as Component, params['gameId'], params['turn']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
