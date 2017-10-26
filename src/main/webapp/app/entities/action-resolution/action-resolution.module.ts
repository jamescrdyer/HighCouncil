import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HighCouncilSharedModule } from '../../shared';
import {
    ActionResolutionService,
    ActionResolutionPopupService,
    ActionResolutionComponent,
    ActionResolutionDetailComponent,
    ActionResolutionDialogComponent,
    ActionResolutionPopupComponent,
    ActionResolutionDeletePopupComponent,
    ActionResolutionDeleteDialogComponent,
    actionResolutionRoute,
    actionResolutionPopupRoute,
} from './';

const ENTITY_STATES = [
    ...actionResolutionRoute,
    ...actionResolutionPopupRoute,
];

@NgModule({
    imports: [
        HighCouncilSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ActionResolutionComponent,
        ActionResolutionDetailComponent,
        ActionResolutionDialogComponent,
        ActionResolutionDeleteDialogComponent,
        ActionResolutionPopupComponent,
        ActionResolutionDeletePopupComponent,
    ],
    entryComponents: [
        ActionResolutionComponent,
        ActionResolutionDialogComponent,
        ActionResolutionPopupComponent,
        ActionResolutionDeleteDialogComponent,
        ActionResolutionDeletePopupComponent,
    ],
    providers: [
        ActionResolutionService,
        ActionResolutionPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HighCouncilActionResolutionModule {}
