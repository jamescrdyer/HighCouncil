/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { HighCouncilTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { KingdomDetailComponent } from '../../../../../../main/webapp/app/entities/kingdom/kingdom-detail.component';
import { KingdomService } from '../../../../../../main/webapp/app/entities/kingdom/kingdom.service';
import { Kingdom } from '../../../../../../main/webapp/app/entities/kingdom/kingdom.model';

describe('Component Tests', () => {

    describe('Kingdom Management Detail Component', () => {
        let comp: KingdomDetailComponent;
        let fixture: ComponentFixture<KingdomDetailComponent>;
        let service: KingdomService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HighCouncilTestModule],
                declarations: [KingdomDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    KingdomService,
                    JhiEventManager
                ]
            }).overrideTemplate(KingdomDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(KingdomDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(KingdomService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Kingdom(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.kingdom).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
