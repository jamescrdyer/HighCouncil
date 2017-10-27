import { BaseEntity } from './../../shared';
import { Player } from './../player';

export const enum Phase {
    'Discussion',
    'Orders',
    'Resolution',
    'Intrigue'
}

export class Game implements BaseEntity {
    constructor(
        public id?: number,
        public timeLimitSeconds?: number,
        public phase?: Phase,
        public players?: Player[],
        public kingdomId?: number,
        public deckId?: number,
    ) {
    }
}
