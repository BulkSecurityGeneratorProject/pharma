/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PharmaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { StoreDetailComponent } from '../../../../../../main/webapp/app/entities/store/store-detail.component';
import { StoreService } from '../../../../../../main/webapp/app/entities/store/store.service';
import { Store } from '../../../../../../main/webapp/app/entities/store/store.model';

describe('Component Tests', () => {

    describe('Store Management Detail Component', () => {
        let comp: StoreDetailComponent;
        let fixture: ComponentFixture<StoreDetailComponent>;
        let service: StoreService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PharmaTestModule],
                declarations: [StoreDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    StoreService,
                    JhiEventManager
                ]
            }).overrideTemplate(StoreDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StoreDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StoreService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Store(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.store).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
