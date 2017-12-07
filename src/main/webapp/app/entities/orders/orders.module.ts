import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HighCouncilSharedModule } from '../../shared';
import {
    OrdersService,
    OrdersPopupService,
    OrdersComponent,
    OrdersDetailComponent,
    OrdersDialogComponent,
    OrdersPopupComponent,
    OrdersDeletePopupComponent,
    OrdersDeleteDialogComponent,
    ordersRoute,
    ordersPopupRoute,
} from './';

const ENTITY_STATES = [
    ...ordersRoute,
    ...ordersPopupRoute,
];

@NgModule({
    imports: [
        HighCouncilSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        OrdersComponent,
        OrdersDetailComponent,
        OrdersDialogComponent,
        OrdersDeleteDialogComponent,
        OrdersPopupComponent,
        OrdersDeletePopupComponent,
    ],
    entryComponents: [
        OrdersComponent,
        OrdersDialogComponent,
        OrdersPopupComponent,
        OrdersDeleteDialogComponent,
        OrdersDeletePopupComponent,
    ],
    providers: [
        OrdersService,
        OrdersPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HighCouncilOrdersModule {}
