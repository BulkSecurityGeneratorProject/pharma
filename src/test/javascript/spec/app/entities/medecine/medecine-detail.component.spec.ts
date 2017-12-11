/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PharmaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MedecineDetailComponent } from '../../../../../../main/webapp/app/entities/medecine/medecine-detail.component';
import { MedecineService } from '../../../../../../main/webapp/app/entities/medecine/medecine.service';
import { Medecine } from '../../../../../../main/webapp/app/entities/medecine/medecine.model';

describe('Component Tests', () => {

    describe('Medecine Management Detail Component', () => {
        let comp: MedecineDetailComponent;
        let fixture: ComponentFixture<MedecineDetailComponent>;
        let service: MedecineService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PharmaTestModule],
                declarations: [MedecineDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MedecineService,
                    JhiEventManager
                ]
            }).overrideTemplate(MedecineDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MedecineDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MedecineService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Medecine(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.medecine).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
