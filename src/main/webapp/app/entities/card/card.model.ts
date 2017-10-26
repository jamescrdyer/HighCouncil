import { BaseEntity } from './../../shared';

export class Card implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public code?: string,
        public numberInDeck?: number,
    ) {
    }
}
