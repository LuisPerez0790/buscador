import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SentencesgwSharedModule } from 'app/shared';
import { HighlightPipe } from 'app/shared/util/highlight';
import {
    SearchSystemService,
    SearchSystemComponent,
    SearchSystemRoute
} from '.';

const ENTITY_STATES = [...SearchSystemRoute];

@NgModule({
    imports: [SentencesgwSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SearchSystemComponent,
        HighlightPipe
    ],
    entryComponents: [SearchSystemComponent],
    providers: [SearchSystemService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SentencesgwSentenceModule {}
