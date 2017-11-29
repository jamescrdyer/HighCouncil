import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';

import { StatContainer } from '../stat-display/stat-container.model';

@Component({
    selector: 'jhi-order-submit',
    styleUrls: [ 'order-submit.component.scss' ],
    templateUrl: './order-submit.component.html'
})
export class OrderSubmitComponent implements OnInit {
    @Input() name: string;
    @Input() numberToSubmit: number;

    @Output() submitted: EventEmitter<StatContainer> = new EventEmitter

    public orders: StatContainer = {
            piety: 0,
            popularity: 0,
            military: 0,
            wealth: 0,
            favour: 0
        };
    public valueSumInvalid = true;
    public isSaving = false;

    constructor(
    ) {
    }

    ngOnInit() {
    }

    checkSum() {
        this.valueSumInvalid = (this.orders.piety + this.orders.popularity + this.orders.wealth + this.orders.military + this.orders.favour)
            !== this.numberToSubmit;
    }

    submit() {
        this.submitted.emit(this.orders);
    }
}
