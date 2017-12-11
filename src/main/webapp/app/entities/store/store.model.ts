import { BaseEntity } from './../../shared';

export class Store implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public country?: string,
        public city?: string,
        public latitude?: number,
        public longitude?: number,
        public datafileContentType?: string,
        public datafile?: any,
        public medecines?: BaseEntity[],
    ) {
    }
}
