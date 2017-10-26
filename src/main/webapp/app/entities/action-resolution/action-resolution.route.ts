import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ActionResolutionComponent } from './action-resolution.component';
import { ActionResolutionDetailComponent } from './action-resolution-detail.component';
import { ActionResolutionPopupComponent } from './action-resolution-dialog.component';
import { ActionResolutionDeletePopupComponent } from './action-resolution-delete-dialog.component';

export const actionResolutionRoute: Routes = [
    {
        path: 'action-resolution',
        component: ActionResolutionComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.actionResolution.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'action-resolution/:id',
        component: ActionResolutionDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.actionResolution.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const actionResolutionPopupRoute: Routes = [
    {
        path: 'action-resolution-new',
        component: ActionResolutionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.actionResolution.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'action-resolution/:id/edit',
        component: ActionResolutionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.actionResolution.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'action-resolution/:id/delete',
        component: ActionResolutionDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.actionResolution.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
