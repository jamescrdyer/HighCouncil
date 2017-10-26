import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HighCouncilSharedModule } from '../../shared';
import {
    DeckService,
    DeckPopupService,
    DeckComponent,
    DeckDetailComponent,
    DeckDialogComponent,
    DeckPopupComponent,
    DeckDeletePopupComponent,
    DeckDeleteDialogComponent,
    deckRoute,
    deckPopupRoute,
} from './';

const ENTITY_STATES = [
    ...deckRoute,
    ...deckPopupRoute,
];

@NgModule({
    imports: [
        HighCouncilSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        DeckComponent,
        DeckDetailComponent,
        DeckDialogComponent,
        DeckDeleteDialogComponent,
        DeckPopupComponent,
        DeckDeletePopupComponent,
    ],
    entryComponents: [
        DeckComponent,
        DeckDialogComponent,
        DeckPopupComponent,
        DeckDeleteDialogComponent,
        DeckDeletePopupComponent,
    ],
    providers: [
        DeckService,
        DeckPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HighCouncilDeckModule {}
