import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Medecine } from './medecine.model';
import { MedecinePopupService } from './medecine-popup.service';
import { MedecineService } from './medecine.service';

@Component({
    selector: 'jhi-medecine-delete-dialog',
    templateUrl: './medecine-delete-dialog.component.html'
})
export class MedecineDeleteDialogComponent {

    medecine: Medecine;

    constructor(
        private medecineService: MedecineService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.medecineService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'medecineListModification',
                content: 'Deleted an medecine'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-medecine-delete-popup',
    template: ''
})
export class MedecineDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private medecinePopupService: MedecinePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.medecinePopupService
                .open(MedecineDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
