import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HighCouncilSharedModule } from '../../shared';
import {
    KingdomService,
    KingdomPopupService,
    KingdomComponent,
    KingdomDetailComponent,
    KingdomDialogComponent,
    KingdomPopupComponent,
    KingdomDeletePopupComponent,
    KingdomDeleteDialogComponent,
    kingdomRoute,
    kingdomPopupRoute,
} from './';

const ENTITY_STATES = [
    ...kingdomRoute,
    ...kingdomPopupRoute,
];

@NgModule({
    imports: [
        HighCouncilSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        KingdomComponent,
        KingdomDetailComponent,
        KingdomDialogComponent,
        KingdomDeleteDialogComponent,
        KingdomPopupComponent,
        KingdomDeletePopupComponent,
    ],
    entryComponents: [
        KingdomComponent,
        KingdomDialogComponent,
        KingdomPopupComponent,
        KingdomDeleteDialogComponent,
        KingdomDeletePopupComponent,
    ],
    providers: [
        KingdomService,
        KingdomPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HighCouncilKingdomModule {}
