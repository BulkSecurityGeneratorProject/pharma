import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { MedecineComponent } from './medecine.component';
import { MedecineDetailComponent } from './medecine-detail.component';
import { MedecinePopupComponent } from './medecine-dialog.component';
import { MedecineDeletePopupComponent } from './medecine-delete-dialog.component';

@Injectable()
export class MedecineResolvePagingParams implements Resolve<any> {

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

export const medecineRoute: Routes = [
    {
        path: 'medecine',
        component: MedecineComponent,
        resolve: {
            'pagingParams': MedecineResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pharmaApp.medecine.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'medecine/:id',
        component: MedecineDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pharmaApp.medecine.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const medecinePopupRoute: Routes = [
    {
        path: 'medecine-new',
        component: MedecinePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pharmaApp.medecine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'medecine/:id/edit',
        component: MedecinePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pharmaApp.medecine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'medecine/:id/delete',
        component: MedecineDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pharmaApp.medecine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
