export interface IResource {
    id?: number;
    fileId?: number;
    title?: string;
    path?: string;
    sentenceId?: number;
}

export class Resource implements IResource {
    constructor(public id?: number, public fileId?: number, public title?: string, public path?: string, public sentenceId?: number) {}
}
