import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { DeckComponent } from './deck.component';
import { DeckDetailComponent } from './deck-detail.component';
import { DeckPopupComponent } from './deck-dialog.component';
import { DeckDeletePopupComponent } from './deck-delete-dialog.component';

export const deckRoute: Routes = [
    {
        path: 'deck',
        component: DeckComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.deck.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'deck/:id',
        component: DeckDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.deck.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const deckPopupRoute: Routes = [
    {
        path: 'deck-new',
        component: DeckPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.deck.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'deck/:id/edit',
        component: DeckPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.deck.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'deck/:id/delete',
        component: DeckDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.deck.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
