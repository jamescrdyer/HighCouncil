import { BaseEntity } from './../../shared';

export const enum Action {
    'Piety',
    'Popularity',
    'Favour',
    'Military',
    'Wealth'
}

export class Orders implements BaseEntity {
    constructor(
        public id?: number,
        public turn?: number,
        public piety?: number,
        public popularity?: number,
        public military?: number,
        public wealth?: number,
        public favour?: number,
        public action?: Action,
        public game?: BaseEntity,
        public player?: BaseEntity,
    ) {
    }
}
