import { Component, ViewChild, AfterViewChecked, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';
import { Principal } from '../../shared/auth/principal.service';

import { Game } from './game.model';
import { GameService } from './game.service';
import { GameDiscussionService } from './game-discussion.service';
import { Message } from './message.model';
import { Player } from '../player/player.model';
import { Orders } from '../orders/orders.model';
import { OrdersService } from '../orders/orders.service';
import { PlayerService } from '../player/player.service';

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
    public ordersSubmitted: Orders = {
            piety: 0,
            popularity: 0,
            military: 0,
            wealth: 0,
            favour: 0
        };

    public discussionDestinations = {};
    public recipientSelected = true;
    public numberOfOrdersToSubmit = 7;
    public messages: Message[] = [];

    constructor(
        private eventManager: JhiEventManager,
        private gameService: GameService,
        private playerService: PlayerService,
        private ordersService: OrdersService,
        private principal: Principal,
        private discussionService: GameDiscussionService,
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
                this.discussionService.receive().subscribe((discussion) => {
                    this.showDiscussion(discussion);
                });
            });
        });
        this.registerChangeInGames();
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
        Object.keys(this.discussionDestinations).forEach((key, index) => {
            if (this.discussionDestinations[key]) {
                toUsers.push(key);
            }
        });
        toUsers.forEach((user) => this.discussionService.sendMessage(user, discussion));
        this.messages.push(discussion);
    }

    load(id) {
        this.gameService.find(id).subscribe((game) => {
            this.game = game;
            this.game.players.forEach((p: Player) => {
                if (this.currentUser !== p.userLogin) {
                    this.discussionDestinations[p.userLogin] = true;
                } else {
                    this.player = p;
                }
            });
        });
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
        this.ordersLocked = !this.ordersLocked;
        this.playerService.setLock(this.game.id, this.ordersLocked);
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
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
        newOrders.game = { id: this.game.id };
        newOrders.player = { id: this.player.id };
        newOrders.turn = this.game.turn;
        if (this.ordersSubmitted.id) {
            newOrders.id = this.ordersSubmitted.id;
            this.ordersService.update(newOrders).subscribe((ordersResult) => this.ordersSubmitted = ordersResult);
        } else {
            this.ordersService.create(newOrders).subscribe((ordersResult) => this.ordersSubmitted = ordersResult);
        }
    }
}
