import { SpyObject } from './spyobject';
import { JhiAlertService } from 'ng-jhipster';
import Spy = jasmine.Spy;

export class MockAlertService extends SpyObject {

    constructor() {
        super(JhiAlertService);
    }

    error() {}
}
