import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Kingdom } from './kingdom.model';
import { KingdomService } from './kingdom.service';

@Component({
    selector: 'jhi-kingdom-detail',
    templateUrl: './kingdom-detail.component.html'
})
export class KingdomDetailComponent implements OnInit, OnDestroy {

    kingdom: Kingdom;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private kingdomService: KingdomService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInKingdoms();
    }

    load(id) {
        this.kingdomService.find(id).subscribe((kingdom) => {
            this.kingdom = kingdom;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInKingdoms() {
        this.eventSubscriber = this.eventManager.subscribe(
            'kingdomListModification',
            (response) => this.load(this.kingdom.id)
        );
    }
}
