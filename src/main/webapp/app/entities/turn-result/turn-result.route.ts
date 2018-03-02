import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TurnResultPopupComponent } from './turn-result-popup.component';

export const turnResultRoute: Routes = [
    {
        path: 'turn-result/:gameId/:turn',
        component: TurnResultPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'highCouncilApp.turnResult.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
