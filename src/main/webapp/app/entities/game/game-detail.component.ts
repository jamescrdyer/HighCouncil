import { Component, ViewChild, AfterViewChecked, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbModule, NgbPopover } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Principal, ResponseWrapper } from '../../shared';

import { Game, Phase } from './game.model';
import { GameService } from './game.service';
import { ActionResolutionByActionFilter } from './action-resolution-by-action-filter.pipe';
import { GameDiscussionService } from './game-discussion.service';
import { Message } from './message.model';
import { Player } from '../player/player.model';
import { TurnResultPopupService } from '../turn-result/turn-result-popup.service';
import { Orders } from '../orders/orders.model';
import { OrdersService } from '../orders/orders.service';
import { PlayerService } from '../player/player.service';
import { ActionResolutionService, ActionResolution, Action } from '../action-resolution';

@Component({
    selector: 'jhi-game-detail',
    styleUrls: [ 'game-detail.component.scss' ],
    templateUrl: './game-detail.component.html'
})
export class GameDetailComponent implements OnInit, OnDestroy, AfterViewChecked {
    game: Game;
    @ViewChild('messageContainer') messageDiv;

    private player: Player;
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    private isScrollPending = false;
    public ordersLocked = false;
    public currentUser: string;
    public actionResolutions: ActionResolution[] = [];
    public hoverAction: Action;
    public ordersSubmitted: Orders = {
            piety: 0,
            popularity: 0,
            military: 0,
            wealth: 0,
            favour: 7
        };

    public discussionDestinations = {};
    public recipientSelected = true;
    public messages: any[] = [];

    constructor(
        private eventManager: JhiEventManager,
        private gameService: GameService,
        private playerService: PlayerService,
        private ordersService: OrdersService,
        private actionResolutionService: ActionResolutionService,
        private principal: Principal,
        private jhiAlertService: JhiAlertService,
        private discussionService: GameDiscussionService,
        private turnResultPopupService: TurnResultPopupService,
        private route: ActivatedRoute
    ) {
    }

    ngAfterViewChecked() {
        if (this.isScrollPending) {
            this.isScrollPending = true;
            this.messageDiv.nativeElement.scrollTop = this.messageDiv.nativeElement.scrollHeight;
        }
    }

    ngOnInit() {
        this.discussionService.connect();
        this.principal.identity().then((account) => this.currentUser = account.login).then(() => {
            this.subscription = this.route.params.subscribe((params) => {
                const gameId = params['id'];
                this.load(gameId);
                this.discussionService.subscribe(gameId);
                this.discussionService.receiveDiscussion().subscribe((discussion) => {
                    this.showDiscussion(discussion);
                });
                this.discussionService.receiveGameState().subscribe((updatedGame) => {
                    this.updateGame(updatedGame);
                });
            });
        });
        this.registerChangeInGames();
        this.loadActionResolutions();
    }

    private updateGame(updatedGame: Game) {
        this.game = updatedGame;
        this.ordersSubmitted = {};
        this.ordersLocked = false;
        this.sortPlayers();
        this.updatedExpectedOrders();
        if (updatedGame.turnResult) {
            this.turnResultPopupService.show(updatedGame.turnResult);
            this.messages.push(updatedGame.turnResult);
        }
    }

    private updatedExpectedOrders() {
        const result = this.game.players.filter((p) => p.id === this.player.id);
        if (result && result.length === 1) {
            const updatedPlayer = (result[0] as Player);
            if (updatedPlayer) {
                this.player.ordersExpected = updatedPlayer.ordersExpected;
            }
        }
    }

    private loadActionResolutions() {
        if (!this.actionResolutions || this.actionResolutions.length === 0) {
            this.actionResolutionService.query({
                page: 0,
                size: 100
            }).subscribe(
                (res: ResponseWrapper) => {
                    this.actionResolutions = [];
                    for (let i = 0; i < res.json.length; i++) {
                        this.actionResolutions.push(res.json[i]);
                    }
                },
                (res: ResponseWrapper) => this.onError(res.json)
            );
        }
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }

    showDiscussion(discussion: Message) {
        if (discussion && discussion.message) {
            this.messages.push(discussion);
            this.isScrollPending = true;
        }
    }

    sendMessage(message: string) {
        const toUsers = [];
        const discussion = new Message(toUsers, message);
        const logins = [];
        Object.keys(this.discussionDestinations).forEach((key, index) => {
            if (this.discussionDestinations[key]) {
                toUsers.push(this.getDisplayNameByLogin(key));
                logins.push(key);
            }
        });
        logins.forEach((user) => this.discussionService.sendMessage(user, discussion));
        this.messages.push(discussion);
    }

    private getDisplayNameByLogin(login: String): String {
        let displayName;
        this.game.players.forEach((p: Player) => {
            if (login === p.userLogin) {
                displayName = p.displayName;
            }
        });
        return displayName;
    }

    load(id) {
        this.gameService.find(id).subscribe((game) => {
            this.game = game;
            this.sortPlayers();
            if (this.game && this.game.players) {
                this.game.players.forEach((p: Player) => {
                    if (this.currentUser !== p.userLogin) {
                        this.discussionDestinations[p.userLogin] = true;
                    } else {
                        this.player = p;
                    }
                });
            }
        });
    }

    sortPlayers() {
        if (this.game && this.game.players) {
            this.game.players.sort(function(p1, p2) {
                if (p1.id < p2.id) {
                    return -1;
                }
                if (p1.id > p2.id) {
                    return 1;
                }
                return 0;
            });
        }
    }

    previousState() {
        window.history.back();
    }

    sendToggle(playerName: string) {
        this.discussionDestinations[playerName] = !this.discussionDestinations[playerName];
        if ((!this.discussionDestinations[playerName] && this.recipientSelected) ||
                (this.discussionDestinations[playerName] && !this.recipientSelected)) {
            this.recipientSelected = false;
            Object.keys(this.discussionDestinations).forEach((key, index) => {
                if (this.discussionDestinations[key]) {
                    this.recipientSelected = true;
                }
            });
        }
    }

    toggleLock() {
        this.playerService.setLock(this.game.id, !this.ordersLocked).subscribe((response) => {
            this.ordersLocked = !this.ordersLocked;
        });
    }

    ngOnDestroy() {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
        this.eventManager.destroy(this.eventSubscriber);
        this.discussionService.unsubscribe();
    }

    registerChangeInGames() {
        this.eventSubscriber = this.eventManager.subscribe(
            'gameListModification',
            (response) => this.load(this.game.id)
        );
    }

    receiveOrders(newOrders: Orders) {
        if (this.game.phase && this.game.phase.valueOf() === 'Orders') {
            newOrders.action = this.ordersSubmitted.action;
            newOrders.id = this.ordersSubmitted.id;
            this.saveOrders(newOrders);
        }
    }

    actionChange(newAction) {
        this.ordersSubmitted.action = newAction;
        this.saveOrders(this.ordersSubmitted);
    }

    setHover(action: Action, sender: any) {
        if (action !== this.hoverAction) {
          this.hoverAction = action;
        }
    }

    private saveOrders(newOrders: Orders) {
        newOrders.turn = this.game.turn;
        newOrders.game = { id: this.game.id };
        newOrders.player = { id: this.player.id };
        if (this.ordersSubmitted.id) {
            this.ordersService.update(newOrders).subscribe((ordersResult) => this.ordersSubmitted = ordersResult);
        } else {
            this.ordersService.create(newOrders).subscribe((ordersResult) => this.ordersSubmitted = ordersResult);
        }
    }
}
