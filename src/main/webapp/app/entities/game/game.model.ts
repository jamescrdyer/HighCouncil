import { BaseEntity } from './../../shared';

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
        public turn?: number,
        public players?: BaseEntity[],
        public kingdomId?: number,
        public deckId?: number,
    ) {
    }
}
