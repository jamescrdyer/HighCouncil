import { BaseEntity } from './../../shared';

export class TurnResult implements BaseEntity {
    constructor(
        public id?: number,
        public piety?: number,
        public popularity?: number,
        public military?: number,
        public wealth?: number,
        public favour?: number,
        public turn?: number,
        public gameId?: number,
        public playerTurnResults?: BaseEntity[],
    ) {
    }
}
