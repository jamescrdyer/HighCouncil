import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HighCouncilSharedModule } from '../../shared';
import {
    TurnResultService,
    TurnResultPopupService,
    TurnResultDialogComponent,
} from './';

@NgModule({
    imports: [
        HighCouncilSharedModule
    ],
    declarations: [
        TurnResultDialogComponent
    ],
    entryComponents: [
        TurnResultDialogComponent,
    ],
    providers: [
        TurnResultService,
        TurnResultPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HighCouncilTurnResultModule {}
