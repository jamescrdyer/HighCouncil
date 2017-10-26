import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('OrderResolution e2e test', () => {

    let navBarPage: NavBarPage;
    let orderResolutionDialogPage: OrderResolutionDialogPage;
    let orderResolutionComponentsPage: OrderResolutionComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load OrderResolutions', () => {
        navBarPage.goToEntity('order-resolution');
        orderResolutionComponentsPage = new OrderResolutionComponentsPage();
        expect(orderResolutionComponentsPage.getTitle()).toMatch(/highCouncilApp.orderResolution.home.title/);

    });

    it('should load create OrderResolution dialog', () => {
        orderResolutionComponentsPage.clickOnCreateButton();
        orderResolutionDialogPage = new OrderResolutionDialogPage();
        expect(orderResolutionDialogPage.getModalTitle()).toMatch(/highCouncilApp.orderResolution.home.createOrEditLabel/);
        orderResolutionDialogPage.close();
    });

    it('should create and save OrderResolutions', () => {
        orderResolutionComponentsPage.clickOnCreateButton();
        orderResolutionDialogPage.setMinimumInput('5');
        expect(orderResolutionDialogPage.getMinimumInput()).toMatch('5');
        orderResolutionDialogPage.setMaximumInput('5');
        expect(orderResolutionDialogPage.getMaximumInput()).toMatch('5');
        orderResolutionDialogPage.save();
        expect(orderResolutionDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class OrderResolutionComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-order-resolution div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class OrderResolutionDialogPage {
    modalTitle = element(by.css('h4#myOrderResolutionLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    minimumInput = element(by.css('input#field_minimum'));
    maximumInput = element(by.css('input#field_maximum'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setMinimumInput = function (minimum) {
        this.minimumInput.sendKeys(minimum);
    }

    getMinimumInput = function () {
        return this.minimumInput.getAttribute('value');
    }

    setMaximumInput = function (maximum) {
        this.maximumInput.sendKeys(maximum);
    }

    getMaximumInput = function () {
        return this.maximumInput.getAttribute('value');
    }

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
