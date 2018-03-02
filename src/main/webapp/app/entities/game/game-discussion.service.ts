import { Injectable } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { Observable, Observer, Subscription } from 'rxjs/Rx';

import { WindowRef } from './../../shared/tracker/window.service';
import { AuthServerProvider } from '../../shared/auth/auth-jwt.service';
import { Principal } from '../../shared/auth/principal.service';

import { Message } from './message.model';

import * as SockJS from 'sockjs-client';
import * as Stomp from 'webstomp-client';

@Injectable()
export class GameDiscussionService {
    stompClient = null;
    subscriberDiscussion = null;
    subscriberGameState = null;
    gameId: number;
    connection: Promise<any>;
    connectedPromise: any;
    listener: Observable<any>;
    stateListener: Observable<any>;
    listenerObserver: Observer<any>;
    stateObserver: Observer<any>;
    alreadyConnectedOnce = false;

    constructor(
        private router: Router,
        private authServerProvider: AuthServerProvider,
        private principal: Principal,
        private $window: WindowRef
    ) {
        this.connection = this.createConnection();
        this.listener = this.createListener();
        this.stateListener = this.createStateListener();
    }

    connect() {
        if (this.connectedPromise === null) {
          this.connection = this.createConnection();
        }
        // building absolute path so that websocket doesn't fail when deploying with a context path
        const loc = this.$window.nativeWindow.location;
        let url;
        url = '//' + loc.host + loc.pathname + 'websocket/discussion';
        const authToken = this.authServerProvider.getToken();
        if (authToken) {
            url += '?access_token=' + authToken;
        }
        const socket = new SockJS(url);
        this.stompClient = Stomp.over(socket);
        const headers = {};
        this.stompClient.connect(headers, () => {
            this.connectedPromise('success');
            this.connectedPromise = null;
            if (!this.alreadyConnectedOnce) {
                this.alreadyConnectedOnce = true;
            }
        });
    }

    disconnect() {
        this.unsubscribe();
        if (this.stompClient !== null) {
            this.stompClient.disconnect();
            this.stompClient = null;
        }
        this.alreadyConnectedOnce = false;
    }

    receiveDiscussion() {
        return this.listener;
    }

    receiveGameState() {
        return this.stateListener;
    }

    sendMessage(recipient: string, message: Message) {
        message.gameId = this.gameId;
        this.principal.identity().then((account) => message.fromUser = account.displayName).then(() => {
            if (this.stompClient !== null && this.stompClient.connected) {
                this.stompClient.send(
                    '/user/' + recipient + '/topic/discussion/' + this.gameId, // destination
                    JSON.stringify(message), // body
                    {} // header
                );
            }
        });
    }

    subscribe(gameId: number) {
        this.gameId = gameId;
        this.connection.then(() => {
            this.subscriberDiscussion = this.stompClient.subscribe('/user/topic/discussion/' + gameId, (data) => {
                this.listenerObserver.next(JSON.parse(data.body));
            });
            this.subscriberGameState = this.stompClient.subscribe('/topic/gamestate/' + gameId, (data) => {
                this.stateObserver.next(JSON.parse(data.body));
            });
        });
    }

    unsubscribe() {
        if (this.subscriberDiscussion !== null) {
            this.subscriberDiscussion.unsubscribe();
        }
        if (this.subscriberGameState !== null) {
            this.subscriberGameState.unsubscribe();
        }
        this.listener = this.createListener();
        this.stateListener = this.createStateListener();
    }

    private createListener(): Observable<any> {
        return new Observable((observer) => {
            this.listenerObserver = observer;
        });
    }

    private createStateListener(): Observable<any> {
        return new Observable((observer) => {
            this.stateObserver = observer;
        });
    }

    private createConnection(): Promise<any> {
        return new Promise((resolve, reject) => this.connectedPromise = resolve);
    }
}
