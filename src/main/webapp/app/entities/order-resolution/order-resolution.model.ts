import { BaseEntity } from './../../shared';

export class OrderResolution implements BaseEntity {
    constructor(
        public id?: number,
        public minimum?: number,
        public maximum?: number,
    ) {
    }
}
