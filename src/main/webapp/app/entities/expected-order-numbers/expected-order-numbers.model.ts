import { BaseEntity } from './../../shared';

export class ExpectedOrderNumbers implements BaseEntity {
    constructor(
        public id?: number,
        public numberOfPlayers?: number,
        public playerNumber?: number,
        public ordersExpected?: number,
    ) {
    }
}
