import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { HighCouncilSharedModule } from '../../shared';
import {
    StatDisplayComponent,
} from './';

@NgModule({
    imports: [
        HighCouncilSharedModule,
    ],
    declarations: [
        StatDisplayComponent,
    ],
    entryComponents: [
    ],
    providers: [
    ],
    exports: [
        StatDisplayComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HighCouncilStatDisplayModule {}
