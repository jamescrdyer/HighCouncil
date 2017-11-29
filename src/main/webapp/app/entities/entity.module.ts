import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HighCouncilGameModule } from './game/game.module';
import { HighCouncilPlayerModule } from './player/player.module';
import { HighCouncilDeckModule } from './deck/deck.module';
import { HighCouncilKingdomModule } from './kingdom/kingdom.module';
import { HighCouncilCardModule } from './card/card.module';
import { HighCouncilOrderResolutionModule } from './order-resolution/order-resolution.module';
import { HighCouncilActionResolutionModule } from './action-resolution/action-resolution.module';
import { HighCouncilStatDisplayModule } from './stat-display/stat-display.module';
import { HighCouncilOrderSubmitModule } from './order-submit/order-submit.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        CommonModule,
        HighCouncilGameModule,
        HighCouncilPlayerModule,
        HighCouncilDeckModule,
        HighCouncilKingdomModule,
        HighCouncilCardModule,
        HighCouncilOrderResolutionModule,
        HighCouncilActionResolutionModule,
        HighCouncilStatDisplayModule,
        HighCouncilOrderSubmitModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [
    ],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HighCouncilEntityModule {}
