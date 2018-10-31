import { IResource } from 'app/shared/model//resource.model';

export interface ISentence {
    id?: number;
    title?: string;
    group?: string;
    country?: string;
    status?: string;
    emisor?: string;
    facts?: string;
    argumentsSummary?: string;
    type?: number;
    resources?: IResource[];
}

export class Sentence implements ISentence {
    constructor(
        public id?: number,
        public title?: string,
        public group?: string,
        public country?: string,
        public status?: string,
        public emisor?: string,
        public facts?: string,
        public argumentsSummary?: string,
        public type?: number,
        public resources?: IResource[]
    ) {}
}
