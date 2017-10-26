import { Component, Input, OnInit } from '@angular/core';

import { StatContainer } from './stat-container.model';

@Component({
    selector: 'jhi-stat-display',
    styleUrls: [ 'stat-display.component.scss' ],
    templateUrl: './stat-display.component.html'
})
export class StatDisplayComponent implements OnInit {
    @Input() name: string;
    @Input() stats: StatContainer;
    @Input() mostStats: StatContainer;
    @Input() leastStats: StatContainer;

    constructor(
    ) {
    }

    ngOnInit() {
    }
}
