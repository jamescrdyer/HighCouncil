/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { HighCouncilTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MockPrincipal } from '../../../helpers/mock-principal.service';
import { Principal } from '../../../../../../main/webapp/app/shared/auth/principal.service';
import { GameDetailComponent } from '../../../../../../main/webapp/app/entities/game/game-detail.component';
import { GameService } from '../../../../../../main/webapp/app/entities/game/game.service';
import { GameDiscussionService } from '../../../../../../main/webapp/app/entities/game/game-discussion.service';
import { PlayerService } from '../../../../../../main/webapp/app/entities/player/player.service';
import { OrdersService } from '../../../../../../main/webapp/app/entities/orders/orders.service';
import { Game } from '../../../../../../main/webapp/app/entities/game/game.model';

describe('Component Tests', () => {

    describe('Game Management Detail Component', () => {
        let comp: GameDetailComponent;
        let fixture: ComponentFixture<GameDetailComponent>;
        let service: GameService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [HighCouncilTestModule],
                declarations: [GameDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    GameService,
                    {
                        provide: GameDiscussionService,
                        useValue: {
                            connect() {},
                            subscribe() {},
                            unsubscribe() {},
                            receiveDiscussion() { return Observable.of(['first','second'])},
                            receiveGameState() { return Observable.of([])},
                            sendMessage() {}
                        }
                    },
                    PlayerService,
                    OrdersService,
                    {
                        provide: Principal,
                        useValue: new MockPrincipal()
                    },
                    JhiEventManager
                ]
            }).overrideTemplate(GameDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(GameDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GameService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Game(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            // expect(service.find).toHaveBeenCalledWith(123);
            // expect(comp.game).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
