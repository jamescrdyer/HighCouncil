import { BaseEntity } from './../../shared';

export const enum Action {
    'Piety',
    'Popularity',
    'Indulge',
    'Military',
    'Wealth'
}

export class ActionResolution implements BaseEntity {
    constructor(
        public id?: number,
        public action?: Action,
        public codeNormal?: string,
        public codeChancellor?: string,
        public codeKingdom?: string,
        public orderResolution?: BaseEntity,
    ) {
    }
}
