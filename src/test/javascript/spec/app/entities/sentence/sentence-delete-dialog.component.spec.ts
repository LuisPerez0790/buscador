/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SentencesgwTestModule } from '../../../test.module';
import { SentenceDeleteDialogComponent } from 'app/entities/sentence/sentence-delete-dialog.component';
import { SentenceService } from 'app/entities/sentence/sentence.service';

describe('Component Tests', () => {
    describe('Sentence Management Delete Component', () => {
        let comp: SentenceDeleteDialogComponent;
        let fixture: ComponentFixture<SentenceDeleteDialogComponent>;
        let service: SentenceService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SentencesgwTestModule],
                declarations: [SentenceDeleteDialogComponent]
            })
                .overrideTemplate(SentenceDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SentenceDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SentenceService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
