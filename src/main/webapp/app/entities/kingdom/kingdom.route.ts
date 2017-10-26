import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { KingdomComponent } from './kingdom.component';
import { KingdomDetailComponent } from './kingdom-detail.component';
import { KingdomPopupComponent } from './kingdom-dialog.component';
import { KingdomDeletePopupComponent } from './kingdom-delete-dialog.component';

export const kingdomRoute: Routes = [
    {
        path: 'kingdom',
        component: KingdomComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.kingdom.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'kingdom/:id',
        component: KingdomDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.kingdom.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const kingdomPopupRoute: Routes = [
    {
        path: 'kingdom-new',
        component: KingdomPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.kingdom.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'kingdom/:id/edit',
        component: KingdomPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.kingdom.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'kingdom/:id/delete',
        component: KingdomDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.kingdom.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
