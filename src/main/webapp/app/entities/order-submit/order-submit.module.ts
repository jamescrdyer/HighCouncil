import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { HighCouncilSharedModule } from '../../shared';
import {
    OrderSubmitComponent
} from './';

@NgModule({
    imports: [
        HighCouncilSharedModule,
    ],
    declarations: [
        OrderSubmitComponent
    ],
    entryComponents: [
    ],
    providers: [
    ],
    exports: [
        OrderSubmitComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HighCouncilOrderSubmitModule {}
