import { BaseEntity } from './../../shared';
import { StatContainer } from './../stat-display';

export class Kingdom implements BaseEntity, StatContainer {
    constructor(
        public id?: number,
        public piety?: number,
        public popularity?: number,
        public military?: number,
        public wealth?: number,
        public health?: number,
        public gameId?: number,
    ) {
    }
}
