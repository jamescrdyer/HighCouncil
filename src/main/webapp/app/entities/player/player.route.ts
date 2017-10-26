import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PlayerComponent } from './player.component';
import { PlayerDetailComponent } from './player-detail.component';
import { PlayerPopupComponent } from './player-dialog.component';
import { PlayerDeletePopupComponent } from './player-delete-dialog.component';

export const playerRoute: Routes = [
    {
        path: 'player',
        component: PlayerComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.player.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'player/:id',
        component: PlayerDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.player.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const playerPopupRoute: Routes = [
    {
        path: 'player-new',
        component: PlayerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.player.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'player/:id/edit',
        component: PlayerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.player.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'player/:id/delete',
        component: PlayerDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.player.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
