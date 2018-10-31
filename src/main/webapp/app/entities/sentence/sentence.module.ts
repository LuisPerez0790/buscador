import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SentencesgwSharedModule } from 'app/shared';
import {
    SentenceComponent,
    SentenceDetailComponent,
    SentenceUpdateComponent,
    SentenceDeletePopupComponent,
    SentenceDeleteDialogComponent,
    sentenceRoute,
    sentencePopupRoute
} from './';

const ENTITY_STATES = [...sentenceRoute, ...sentencePopupRoute];

@NgModule({
    imports: [SentencesgwSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SentenceComponent,
        SentenceDetailComponent,
        SentenceUpdateComponent,
        SentenceDeleteDialogComponent,
        SentenceDeletePopupComponent
    ],
    entryComponents: [SentenceComponent, SentenceUpdateComponent, SentenceDeleteDialogComponent, SentenceDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SentencesgwSentenceModule {}
