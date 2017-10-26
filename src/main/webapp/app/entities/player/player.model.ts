import { BaseEntity } from './../../shared';

export class Player implements BaseEntity {
    constructor(
        public id?: number,
        public piety?: number,
        public popularity?: number,
        public military?: number,
        public wealth?: number,
        public favour?: number,
        public chancellor?: boolean,
        public name?: string,
        public gameId?: number,
        public userId?: number,
        public hands?: BaseEntity[],
    ) {
        this.chancellor = false;
    }
}
