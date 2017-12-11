import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Medecine } from './medecine.model';
import { MedecineService } from './medecine.service';

@Injectable()
export class MedecinePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private medecineService: MedecineService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.medecineService.find(id).subscribe((medecine) => {
                    if (medecine.lastupdate) {
                        medecine.lastupdate = {
                            year: medecine.lastupdate.getFullYear(),
                            month: medecine.lastupdate.getMonth() + 1,
                            day: medecine.lastupdate.getDate()
                        };
                    }
                    this.ngbModalRef = this.medecineModalRef(component, medecine);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.medecineModalRef(component, new Medecine());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    medecineModalRef(component: Component, medecine: Medecine): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.medecine = medecine;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
