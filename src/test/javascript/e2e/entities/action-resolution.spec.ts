import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('ActionResolution e2e test', () => {

    let navBarPage: NavBarPage;
    let actionResolutionDialogPage: ActionResolutionDialogPage;
    let actionResolutionComponentsPage: ActionResolutionComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load ActionResolutions', () => {
        navBarPage.goToEntity('action-resolution');
        actionResolutionComponentsPage = new ActionResolutionComponentsPage();
        expect(actionResolutionComponentsPage.getTitle()).toMatch(/highCouncilApp.actionResolution.home.title/);

    });

    it('should load create ActionResolution dialog', () => {
        actionResolutionComponentsPage.clickOnCreateButton();
        actionResolutionDialogPage = new ActionResolutionDialogPage();
        expect(actionResolutionDialogPage.getModalTitle()).toMatch(/highCouncilApp.actionResolution.home.createOrEditLabel/);
        actionResolutionDialogPage.close();
    });

    it('should create and save ActionResolutions', () => {
        actionResolutionComponentsPage.clickOnCreateButton();
        actionResolutionDialogPage.actionSelectLastOption();
        actionResolutionDialogPage.setCodeNormalInput('codeNormal');
        expect(actionResolutionDialogPage.getCodeNormalInput()).toMatch('codeNormal');
        actionResolutionDialogPage.setCodeChancellorInput('codeChancellor');
        expect(actionResolutionDialogPage.getCodeChancellorInput()).toMatch('codeChancellor');
        actionResolutionDialogPage.setCodeKingdomInput('codeKingdom');
        expect(actionResolutionDialogPage.getCodeKingdomInput()).toMatch('codeKingdom');
        actionResolutionDialogPage.orderResolutionSelectLastOption();
        actionResolutionDialogPage.save();
        expect(actionResolutionDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ActionResolutionComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-action-resolution div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ActionResolutionDialogPage {
    modalTitle = element(by.css('h4#myActionResolutionLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    actionSelect = element(by.css('select#field_action'));
    codeNormalInput = element(by.css('input#field_codeNormal'));
    codeChancellorInput = element(by.css('input#field_codeChancellor'));
    codeKingdomInput = element(by.css('input#field_codeKingdom'));
    orderResolutionSelect = element(by.css('select#field_orderResolution'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setActionSelect = function (action) {
        this.actionSelect.sendKeys(action);
    }

    getActionSelect = function () {
        return this.actionSelect.element(by.css('option:checked')).getText();
    }

    actionSelectLastOption = function () {
        this.actionSelect.all(by.tagName('option')).last().click();
    }
    setCodeNormalInput = function (codeNormal) {
        this.codeNormalInput.sendKeys(codeNormal);
    }

    getCodeNormalInput = function () {
        return this.codeNormalInput.getAttribute('value');
    }

    setCodeChancellorInput = function (codeChancellor) {
        this.codeChancellorInput.sendKeys(codeChancellor);
    }

    getCodeChancellorInput = function () {
        return this.codeChancellorInput.getAttribute('value');
    }

    setCodeKingdomInput = function (codeKingdom) {
        this.codeKingdomInput.sendKeys(codeKingdom);
    }

    getCodeKingdomInput = function () {
        return this.codeKingdomInput.getAttribute('value');
    }

    orderResolutionSelectLastOption = function () {
        this.orderResolutionSelect.all(by.tagName('option')).last().click();
    }

    orderResolutionSelectOption = function (option) {
        this.orderResolutionSelect.sendKeys(option);
    }

    getOrderResolutionSelect = function () {
        return this.orderResolutionSelect;
    }

    getOrderResolutionSelectedOption = function () {
        return this.orderResolutionSelect.element(by.css('option:checked')).getText();
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
