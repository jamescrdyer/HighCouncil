import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { OrdersComponent } from './orders.component';
import { OrdersDetailComponent } from './orders-detail.component';
import { OrdersPopupComponent } from './orders-dialog.component';
import { OrdersDeletePopupComponent } from './orders-delete-dialog.component';

export const ordersRoute: Routes = [
    {
        path: 'orders',
        component: OrdersComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.orders.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'orders/:id',
        component: OrdersDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.orders.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ordersPopupRoute: Routes = [
    {
        path: 'orders-new',
        component: OrdersPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.orders.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'orders/:id/edit',
        component: OrdersPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.orders.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'orders/:id/delete',
        component: OrdersDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.orders.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
