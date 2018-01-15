import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ExpectedOrderNumbersComponent } from './expected-order-numbers.component';
import { ExpectedOrderNumbersDetailComponent } from './expected-order-numbers-detail.component';
import { ExpectedOrderNumbersPopupComponent } from './expected-order-numbers-dialog.component';
import { ExpectedOrderNumbersDeletePopupComponent } from './expected-order-numbers-delete-dialog.component';

@Injectable()
export class ExpectedOrderNumbersResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const expectedOrderNumbersRoute: Routes = [
    {
        path: 'expected-order-numbers',
        component: ExpectedOrderNumbersComponent,
        resolve: {
            'pagingParams': ExpectedOrderNumbersResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.expectedOrderNumbers.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'expected-order-numbers/:id',
        component: ExpectedOrderNumbersDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.expectedOrderNumbers.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const expectedOrderNumbersPopupRoute: Routes = [
    {
        path: 'expected-order-numbers-new',
        component: ExpectedOrderNumbersPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.expectedOrderNumbers.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'expected-order-numbers/:id/edit',
        component: ExpectedOrderNumbersPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.expectedOrderNumbers.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'expected-order-numbers/:id/delete',
        component: ExpectedOrderNumbersDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.expectedOrderNumbers.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
