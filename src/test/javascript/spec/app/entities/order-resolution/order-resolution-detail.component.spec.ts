/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { HighCouncilTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { OrderResolutionDetailComponent } from '../../../../../../main/webapp/app/entities/order-resolution/order-resolution-detail.component';
import { OrderResolutionService } from '../../../../../../main/webapp/app/entities/order-resolution/order-resolution.service';
import { OrderResolution } from '../../../../../../main/webapp/app/entities/order-resolution/order-resolution.model';

describe('Component Tests', () => {

    describe('OrderResolution Management Detail Component', () => {
        let comp: OrderResolutionDetailComponent;
        let fixture: ComponentFixture<OrderResolutionDetailComponent>;
        let service: OrderResolutionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HighCouncilTestModule],
                declarations: [OrderResolutionDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    OrderResolutionService,
                    JhiEventManager
                ]
            }).overrideTemplate(OrderResolutionDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OrderResolutionDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrderResolutionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new OrderResolution(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.orderResolution).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
