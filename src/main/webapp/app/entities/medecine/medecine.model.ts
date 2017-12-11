import { BaseEntity } from './../../shared';

export class Medecine implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public lastupdate?: any,
        public store?: BaseEntity,
    ) {
    }
}
