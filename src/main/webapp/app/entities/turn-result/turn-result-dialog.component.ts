import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { TurnResult } from './turn-result.model';

@Component({
    selector: 'jhi-turn-result-dialog',
    templateUrl: './turn-result-dialog.component.html'
})
export class TurnResultDialogComponent implements OnInit {

    turnResult: TurnResult;

    constructor(
        public activeModal: NgbActiveModal
    ) {
    }

    ngOnInit() {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }
}
