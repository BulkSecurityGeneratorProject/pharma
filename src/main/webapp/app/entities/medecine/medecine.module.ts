import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PharmaSharedModule } from '../../shared';
import {
    MedecineService,
    MedecinePopupService,
    MedecineComponent,
    MedecineDetailComponent,
    MedecineDialogComponent,
    MedecinePopupComponent,
    MedecineDeletePopupComponent,
    MedecineDeleteDialogComponent,
    medecineRoute,
    medecinePopupRoute,
    MedecineResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...medecineRoute,
    ...medecinePopupRoute,
];

@NgModule({
    imports: [
        PharmaSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        MedecineComponent,
        MedecineDetailComponent,
        MedecineDialogComponent,
        MedecineDeleteDialogComponent,
        MedecinePopupComponent,
        MedecineDeletePopupComponent,
    ],
    entryComponents: [
        MedecineComponent,
        MedecineDialogComponent,
        MedecinePopupComponent,
        MedecineDeleteDialogComponent,
        MedecineDeletePopupComponent,
    ],
    providers: [
        MedecineService,
        MedecinePopupService,
        MedecineResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PharmaMedecineModule {}
