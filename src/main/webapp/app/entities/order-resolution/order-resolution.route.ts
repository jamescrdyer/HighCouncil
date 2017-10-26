import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { OrderResolutionComponent } from './order-resolution.component';
import { OrderResolutionDetailComponent } from './order-resolution-detail.component';
import { OrderResolutionPopupComponent } from './order-resolution-dialog.component';
import { OrderResolutionDeletePopupComponent } from './order-resolution-delete-dialog.component';

export const orderResolutionRoute: Routes = [
    {
        path: 'order-resolution',
        component: OrderResolutionComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.orderResolution.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'order-resolution/:id',
        component: OrderResolutionDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.orderResolution.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const orderResolutionPopupRoute: Routes = [
    {
        path: 'order-resolution-new',
        component: OrderResolutionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.orderResolution.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'order-resolution/:id/edit',
        component: OrderResolutionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.orderResolution.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'order-resolution/:id/delete',
        component: OrderResolutionDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.orderResolution.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
