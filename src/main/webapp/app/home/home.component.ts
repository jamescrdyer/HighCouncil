import { Component, OnInit, OnDestroy } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs/Rx';

import { Account, LoginModalService, Principal, ITEMS_PER_PAGE, ResponseWrapper } from '../shared';
import { GameService, Game } from '../entities/game';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.scss'
    ]

})
export class HomeComponent implements OnInit, OnDestroy {
    account: Account;
    modalRef: NgbModalRef;
    formingGames: Game[];

    currentAccount: any;
    itemsPerPage: number;
    links: any;
    page: any;
    queryCount: any;
    totalItems: number;
    isSaving: boolean;
    getGamesSubscription: Subscription;

    constructor(
        private principal: Principal,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private loginModalService: LoginModalService,
        private gameService: GameService,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.formingGames = [];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
        this.getGamesSubscription = Observable.timer(8000).subscribe(() => {
            if (this.isAuthenticated()) {
                this.loadAll();
            }
        });
    }
    ngOnDestroy() {
        if (this.getGamesSubscription) {
            this.getGamesSubscription.unsubscribe();
        }
    }

    join(game: Game) {
        this.subscribeToSaveResponse(
                this.gameService.join(game));
    }

    private subscribeToSaveResponse(result: Observable<Game>) {
        result.subscribe((res: Game) =>
            this.onJoinSuccess(res), (res: Response) => this.onJoinError());
    }

    private onJoinSuccess(result: Game) {
        this.isSaving = false;
        this.router.navigate(['/game', result.id]);
    }

    private onJoinError() {
        this.isSaving = false;
    }

    reset() {
        this.page = 0;
        this.formingGames = [];
        this.loadAll();
    }

    loadAll() {
        this.gameService.getForming({
            page: this.page,
            size: this.itemsPerPage,
            sort: ['id']
        }).subscribe(
            (res: ResponseWrapper) => this.onLoadSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onLoadError(res.json)
        );
        if (this.account && !this.account.displayName) {
            console.log('No display name, ' + this.account.displayName);
            this.router.navigate(['/settings']);
        }
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
                this.loadAll();
            });
        });
    }

    isAlreadyPlaying(game) {
        for (let i = 0; i < game.players.length; i++) {
            const p = game.players[i];
            if (p.userLogin === this.account.login) {
                return true;
            }
        }
        return false;
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    private onLoadSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        for (let i = 0; i < data.length; i++) {
            this.formingGames.push(data[i]);
        }
    }

    private onLoadError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackId(index: number, item: Game) {
        return item.id;
    }
}
