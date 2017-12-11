import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Medecine } from './medecine.model';
import { MedecinePopupService } from './medecine-popup.service';
import { MedecineService } from './medecine.service';
import { Store, StoreService } from '../store';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-medecine-dialog',
    templateUrl: './medecine-dialog.component.html'
})
export class MedecineDialogComponent implements OnInit {

    medecine: Medecine;
    isSaving: boolean;

    stores: Store[];
    lastupdateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private medecineService: MedecineService,
        private storeService: StoreService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.storeService.query()
            .subscribe((res: ResponseWrapper) => { this.stores = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.medecine.id !== undefined) {
            this.subscribeToSaveResponse(
                this.medecineService.update(this.medecine));
        } else {
            this.subscribeToSaveResponse(
                this.medecineService.create(this.medecine));
        }
    }

    private subscribeToSaveResponse(result: Observable<Medecine>) {
        result.subscribe((res: Medecine) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Medecine) {
        this.eventManager.broadcast({ name: 'medecineListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackStoreById(index: number, item: Store) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-medecine-popup',
    template: ''
})
export class MedecinePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private medecinePopupService: MedecinePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.medecinePopupService
                    .open(MedecineDialogComponent as Component, params['id']);
            } else {
                this.medecinePopupService
                    .open(MedecineDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
