/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { HighCouncilTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { OrdersDetailComponent } from '../../../../../../main/webapp/app/entities/orders/orders-detail.component';
import { OrdersService } from '../../../../../../main/webapp/app/entities/orders/orders.service';
import { Orders } from '../../../../../../main/webapp/app/entities/orders/orders.model';

describe('Component Tests', () => {

    describe('Orders Management Detail Component', () => {
        let comp: OrdersDetailComponent;
        let fixture: ComponentFixture<OrdersDetailComponent>;
        let service: OrdersService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HighCouncilTestModule],
                declarations: [OrdersDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    OrdersService,
                    JhiEventManager
                ]
            }).overrideTemplate(OrdersDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OrdersDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrdersService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Orders(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.orders).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
