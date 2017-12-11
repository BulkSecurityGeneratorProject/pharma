import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Medecine } from './medecine.model';
import { MedecineService } from './medecine.service';

@Component({
    selector: 'jhi-medecine-detail',
    templateUrl: './medecine-detail.component.html'
})
export class MedecineDetailComponent implements OnInit, OnDestroy {

    medecine: Medecine;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private medecineService: MedecineService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMedecines();
    }

    load(id) {
        this.medecineService.find(id).subscribe((medecine) => {
            this.medecine = medecine;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMedecines() {
        this.eventSubscriber = this.eventManager.subscribe(
            'medecineListModification',
            (response) => this.load(this.medecine.id)
        );
    }
}
