import { BaseEntity } from './../../shared';

export class Orders implements BaseEntity {
    constructor(
        public id?: number,
        public turn?: number,
        public piety?: number,
        public popularity?: number,
        public military?: number,
        public wealth?: number,
        public favour?: number,
        public game?: BaseEntity,
        public player?: BaseEntity,
    ) {
    }
}
