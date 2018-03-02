import { Component, OnInit } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { TurnResult } from './turn-result.model';
import { PlayerTurnResult } from './player-turn-result.model';

@Component({
    selector: 'jhi-turn-result-dialog',
    styleUrls: [ 'turn-result-dialog.component.scss' ],
    templateUrl: './turn-result-dialog.component.html'
})
export class TurnResultDialogComponent implements OnInit {

    turnResult: TurnResult;

    constructor(
        public activeModal: NgbActiveModal
    ) {
    }

    ngOnInit() {
        this.turnResult.playerTurnResults.sort(function(p1, p2) {
            const ptr1 = p1 as PlayerTurnResult;
            const ptr2 = p2 as PlayerTurnResult;

            if (!ptr1 || !ptr1.playerId) {
                return -1;
            }
            if (!ptr2 || !ptr2.playerId) {
                return 1;
            }
            if (ptr1.playerId < ptr2.playerId) {
                return -1;
            }
            if (ptr1.playerId > ptr2.playerId) {
                return 1;
            }
            return 0;
        });
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }
}
