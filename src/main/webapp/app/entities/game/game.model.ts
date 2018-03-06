import { BaseEntity } from './../../shared';

export const enum Phase {
    Forming = 'Forming',
    Orders = 'Orders',
    Intrigue = 'Intrigue',
    Completed = 'Completed'
}

export class Game implements BaseEntity {
    constructor(
        public id?: number,
        public timeLimitSeconds?: number,
        public phase?: Phase,
        public turn?: number,
        public randomOrderNumber?: number,
        public playersNumber?: number,
        public players?: BaseEntity[],
        public turnResult?: BaseEntity,
        public kingdomId?: number,
        public deckId?: number,
    ) {
    }
}
