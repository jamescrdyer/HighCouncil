import { BaseEntity } from './../../shared';

export const enum Action {
    'Piety',
    ' Popularity',
    ' Favour',
    ' Military',
    ' Wealth'
}

export class PlayerTurnResult implements BaseEntity {
    constructor(
        public id?: number,
        public piety?: number,
        public popularity?: number,
        public military?: number,
        public wealth?: number,
        public favour?: number,
        public penalty?: number,
        public turn?: number,
        public action?: Action,
        public gameId?: number,
        public playerId?: number,
        public turnResultId?: number,
    ) {
    }
}
