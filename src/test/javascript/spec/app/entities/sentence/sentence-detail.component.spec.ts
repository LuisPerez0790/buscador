/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SentencesgwTestModule } from '../../../test.module';
import { SentenceDetailComponent } from 'app/entities/sentence/sentence-detail.component';
import { Sentence } from 'app/shared/model/sentence.model';

describe('Component Tests', () => {
    describe('Sentence Management Detail Component', () => {
        let comp: SentenceDetailComponent;
        let fixture: ComponentFixture<SentenceDetailComponent>;
        const route = ({ data: of({ sentence: new Sentence(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SentencesgwTestModule],
                declarations: [SentenceDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SentenceDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SentenceDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.sentence).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
