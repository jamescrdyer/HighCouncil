import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { HighCouncilSharedModule } from '../../shared';
import { HighCouncilStatDisplayModule } from '../stat-display';
import { HighCouncilOrderSubmitModule } from '../order-submit';
import {
    GameService,
    GameDiscussionService,
    GamePopupService,
    GameComponent,
    GameDetailComponent,
    GameDialogComponent,
    GamePopupComponent,
    GameDeletePopupComponent,
    GameDeleteDialogComponent,
    ActionResolutionByActionFilter,
    gameRoute,
    gamePopupRoute,
} from './';

const ENTITY_STATES = [
    ...gameRoute,
    ...gamePopupRoute,
];

@NgModule({
    imports: [
        HighCouncilSharedModule,
        HighCouncilStatDisplayModule,
        HighCouncilOrderSubmitModule,
        NgbModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        GameComponent,
        GameDetailComponent,
        GameDialogComponent,
        GameDeleteDialogComponent,
        GamePopupComponent,
        GameDeletePopupComponent,
        ActionResolutionByActionFilter,
    ],
    entryComponents: [
        GameComponent,
        GameDialogComponent,
        GamePopupComponent,
        GameDeleteDialogComponent,
        GameDeletePopupComponent,
    ],
    providers: [
        GameService,
        GameDiscussionService,
        GamePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HighCouncilGameModule {}
