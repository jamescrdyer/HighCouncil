import { BaseEntity } from './../../shared';

export class Deck implements BaseEntity {
    constructor(
        public id?: number,
        public gameId?: number,
        public cards?: BaseEntity[],
        public discards?: BaseEntity[],
    ) {
    }
}
