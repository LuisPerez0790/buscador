
export interface IFilters {
    country?: string[];
    group?: string[];
}

export class Filters implements IFilters {
    constructor(
        public country?: string[],
        public group?: string[],
    ) {}
}
