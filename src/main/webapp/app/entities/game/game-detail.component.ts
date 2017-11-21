import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Game } from './game.model';
import { GameService } from './game.service';
import { GameDiscussionService } from './game-discussion.service';
import { Message } from './message.model';

@Component({
    selector: 'jhi-game-detail',
    templateUrl: './game-detail.component.html'
})
export class GameDetailComponent implements OnInit, OnDestroy {

    game: Game;
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    public discussionDestinations = {};
    public messages: Message[] = [];

    constructor(
        private eventManager: JhiEventManager,
        private gameService: GameService,
        private discussionService: GameDiscussionService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.discussionService.connect();
        this.subscription = this.route.params.subscribe((params) => {
            const gameId = params['id'];
            this.load(gameId);
            this.discussionService.subscribe(gameId);
            this.discussionService.receive().subscribe((discussion) => {
                this.showDiscussion(discussion);
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
                this.discussionDestinations[p.userLogin] = true;
            });
        });
    }
    previousState() {
        window.history.back();
    }

    sendToggle(playerName: string) {
        this.discussionDestinations[playerName] = !this.discussionDestinations[playerName];
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
}
