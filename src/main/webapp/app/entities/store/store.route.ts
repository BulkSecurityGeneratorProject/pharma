import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { StoreComponent } from './store.component';
import { StoreDetailComponent } from './store-detail.component';
import { StorePopupComponent } from './store-dialog.component';
import { StoreDeletePopupComponent } from './store-delete-dialog.component';

@Injectable()
export class StoreResolvePagingParams implements Resolve<any> {

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

export const storeRoute: Routes = [
    {
        path: 'store',
        component: StoreComponent,
        resolve: {
            'pagingParams': StoreResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pharmaApp.store.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'store/:id',
        component: StoreDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pharmaApp.store.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const storePopupRoute: Routes = [
    {
        path: 'store-new',
        component: StorePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pharmaApp.store.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'store/:id/edit',
        component: StorePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pharmaApp.store.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'store/:id/delete',
        component: StoreDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pharmaApp.store.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
