/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { HighCouncilTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ActionResolutionDetailComponent } from '../../../../../../main/webapp/app/entities/action-resolution/action-resolution-detail.component';
import { ActionResolutionService } from '../../../../../../main/webapp/app/entities/action-resolution/action-resolution.service';
import { ActionResolution } from '../../../../../../main/webapp/app/entities/action-resolution/action-resolution.model';

describe('Component Tests', () => {

    describe('ActionResolution Management Detail Component', () => {
        let comp: ActionResolutionDetailComponent;
        let fixture: ComponentFixture<ActionResolutionDetailComponent>;
        let service: ActionResolutionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HighCouncilTestModule],
                declarations: [ActionResolutionDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ActionResolutionService,
                    JhiEventManager
                ]
            }).overrideTemplate(ActionResolutionDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ActionResolutionDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ActionResolutionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ActionResolution(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.actionResolution).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
