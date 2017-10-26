/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { HighCouncilTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { DeckDetailComponent } from '../../../../../../main/webapp/app/entities/deck/deck-detail.component';
import { DeckService } from '../../../../../../main/webapp/app/entities/deck/deck.service';
import { Deck } from '../../../../../../main/webapp/app/entities/deck/deck.model';

describe('Component Tests', () => {

    describe('Deck Management Detail Component', () => {
        let comp: DeckDetailComponent;
        let fixture: ComponentFixture<DeckDetailComponent>;
        let service: DeckService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HighCouncilTestModule],
                declarations: [DeckDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    DeckService,
                    JhiEventManager
                ]
            }).overrideTemplate(DeckDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DeckDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DeckService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Deck(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.deck).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
