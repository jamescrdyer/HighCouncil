import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HighCouncilSharedModule } from '../../shared';
import {
    ExpectedOrderNumbersService,
    ExpectedOrderNumbersPopupService,
    ExpectedOrderNumbersComponent,
    ExpectedOrderNumbersDetailComponent,
    ExpectedOrderNumbersDialogComponent,
    ExpectedOrderNumbersPopupComponent,
    ExpectedOrderNumbersDeletePopupComponent,
    ExpectedOrderNumbersDeleteDialogComponent,
    expectedOrderNumbersRoute,
    expectedOrderNumbersPopupRoute,
    ExpectedOrderNumbersResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...expectedOrderNumbersRoute,
    ...expectedOrderNumbersPopupRoute,
];

@NgModule({
    imports: [
        HighCouncilSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ExpectedOrderNumbersComponent,
        ExpectedOrderNumbersDetailComponent,
        ExpectedOrderNumbersDialogComponent,
        ExpectedOrderNumbersDeleteDialogComponent,
        ExpectedOrderNumbersPopupComponent,
        ExpectedOrderNumbersDeletePopupComponent,
    ],
    entryComponents: [
        ExpectedOrderNumbersComponent,
        ExpectedOrderNumbersDialogComponent,
        ExpectedOrderNumbersPopupComponent,
        ExpectedOrderNumbersDeleteDialogComponent,
        ExpectedOrderNumbersDeletePopupComponent,
    ],
    providers: [
        ExpectedOrderNumbersService,
        ExpectedOrderNumbersPopupService,
        ExpectedOrderNumbersResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HighCouncilExpectedOrderNumbersModule {}
