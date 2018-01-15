/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { HighCouncilTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ExpectedOrderNumbersDetailComponent } from '../../../../../../main/webapp/app/entities/expected-order-numbers/expected-order-numbers-detail.component';
import { ExpectedOrderNumbersService } from '../../../../../../main/webapp/app/entities/expected-order-numbers/expected-order-numbers.service';
import { ExpectedOrderNumbers } from '../../../../../../main/webapp/app/entities/expected-order-numbers/expected-order-numbers.model';

describe('Component Tests', () => {

    describe('ExpectedOrderNumbers Management Detail Component', () => {
        let comp: ExpectedOrderNumbersDetailComponent;
        let fixture: ComponentFixture<ExpectedOrderNumbersDetailComponent>;
        let service: ExpectedOrderNumbersService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HighCouncilTestModule],
                declarations: [ExpectedOrderNumbersDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ExpectedOrderNumbersService,
                    JhiEventManager
                ]
            }).overrideTemplate(ExpectedOrderNumbersDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ExpectedOrderNumbersDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ExpectedOrderNumbersService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ExpectedOrderNumbers(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.expectedOrderNumbers).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
