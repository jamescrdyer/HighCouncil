import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HighCouncilSharedModule } from '../../shared';
import {
    OrderResolutionService,
    OrderResolutionPopupService,
    OrderResolutionComponent,
    OrderResolutionDetailComponent,
    OrderResolutionDialogComponent,
    OrderResolutionPopupComponent,
    OrderResolutionDeletePopupComponent,
    OrderResolutionDeleteDialogComponent,
    orderResolutionRoute,
    orderResolutionPopupRoute,
} from './';

const ENTITY_STATES = [
    ...orderResolutionRoute,
    ...orderResolutionPopupRoute,
];

@NgModule({
    imports: [
        HighCouncilSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        OrderResolutionComponent,
        OrderResolutionDetailComponent,
        OrderResolutionDialogComponent,
        OrderResolutionDeleteDialogComponent,
        OrderResolutionPopupComponent,
        OrderResolutionDeletePopupComponent,
    ],
    entryComponents: [
        OrderResolutionComponent,
        OrderResolutionDialogComponent,
        OrderResolutionPopupComponent,
        OrderResolutionDeleteDialogComponent,
        OrderResolutionDeletePopupComponent,
    ],
    providers: [
        OrderResolutionService,
        OrderResolutionPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HighCouncilOrderResolutionModule {}
