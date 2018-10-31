/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SentencesgwTestModule } from '../../../test.module';
import { SentenceUpdateComponent } from 'app/entities/sentence/sentence-update.component';
import { SentenceService } from 'app/entities/sentence/sentence.service';
import { Sentence } from 'app/shared/model/sentence.model';

describe('Component Tests', () => {
    describe('Sentence Management Update Component', () => {
        let comp: SentenceUpdateComponent;
        let fixture: ComponentFixture<SentenceUpdateComponent>;
        let service: SentenceService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SentencesgwTestModule],
                declarations: [SentenceUpdateComponent]
            })
                .overrideTemplate(SentenceUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SentenceUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SentenceService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Sentence(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.sentence = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Sentence();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.sentence = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
