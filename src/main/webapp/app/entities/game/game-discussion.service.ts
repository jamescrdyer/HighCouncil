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
    subscriber = null;
    gameId: number;
    connection: Promise<any>;
    connectedPromise: any;
    listener: Observable<any>;
    listenerObserver: Observer<any>;
    alreadyConnectedOnce = false;
    private subscription: Subscription;

    constructor(
        private router: Router,
        private authServerProvider: AuthServerProvider,
        private principal: Principal,
        private $window: WindowRef
    ) {
        this.connection = this.createConnection();
        this.listener = this.createListener();
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
        if (this.stompClient !== null) {
            this.stompClient.disconnect();
            this.stompClient = null;
        }
        if (this.subscription) {
            this.subscription.unsubscribe();
            this.subscription = null;
        }
        this.alreadyConnectedOnce = false;
    }

    receive() {
        return this.listener;
    }

    sendMessage(recipient: string, message: Message) {
        message.gameId = this.gameId;
        this.principal.identity().then((account) => message.fromUser = account.login).then(() => {
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
            this.subscriber = this.stompClient.subscribe('/user/topic/discussion/' + gameId, (data) => {
                this.listenerObserver.next(JSON.parse(data.body));
            });
        });
    }

    unsubscribe() {
        if (this.subscriber !== null) {
            this.subscriber.unsubscribe();
        }
        this.listener = this.createListener();
    }

    private createListener(): Observable<any> {
        return new Observable((observer) => {
            this.listenerObserver = observer;
        });
    }

    private createConnection(): Promise<any> {
        return new Promise((resolve, reject) => this.connectedPromise = resolve);
    }
}
