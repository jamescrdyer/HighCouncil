import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';
import { Principal } from '../../shared/auth/principal.service';

import { Game } from './game.model';
import { GameService } from './game.service';
import { GameDiscussionService } from './game-discussion.service';
import { Message } from './message.model';
import { StatContainer } from '../stat-display/stat-container.model';

@Component({
    selector: 'jhi-game-detail',
    templateUrl: './game-detail.component.html'
})
export class GameDetailComponent implements OnInit, OnDestroy {
    game: Game;
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    public currentUser: string;
    public ordersSubmitted: StatContainer = {
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
        private principal: Principal,
        private discussionService: GameDiscussionService,
        private route: ActivatedRoute
    ) {
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
    }

    load(id) {
        this.gameService.find(id).subscribe((game) => {
            this.game = game;
            this.game.players.forEach((p) => {
                if (this.currentUser !== p.userLogin) {
                    this.discussionDestinations[p.userLogin] = true;
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

    receiveOrders(newOrders: StatContainer) {
        this.ordersSubmitted = newOrders;
    }
}
