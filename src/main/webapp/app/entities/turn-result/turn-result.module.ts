import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HighCouncilSharedModule } from '../../shared';
import { TurnResultService } from './turn-result.service';
import { TurnResultDialogComponent } from './turn-result-dialog.component';
import { TurnResultPopupComponent } from './turn-result-popup.component';
import { TurnResultPopupService } from './turn-result-popup.service';
import { turnResultRoute } from './turn-result.route';

const ENTITY_STATES = [
    ...turnResultRoute,
];

@NgModule({
    imports: [
        HighCouncilSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TurnResultDialogComponent,
        TurnResultPopupComponent,
    ],
    entryComponents: [
        TurnResultDialogComponent,
        TurnResultPopupComponent,
    ],
    providers: [
        TurnResultService,
        TurnResultPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HighCouncilTurnResultModule {}
