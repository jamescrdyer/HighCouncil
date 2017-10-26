import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Card e2e test', () => {

    let navBarPage: NavBarPage;
    let cardDialogPage: CardDialogPage;
    let cardComponentsPage: CardComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Cards', () => {
        navBarPage.goToEntity('card');
        cardComponentsPage = new CardComponentsPage();
        expect(cardComponentsPage.getTitle()).toMatch(/highCouncilApp.card.home.title/);

    });

    it('should load create Card dialog', () => {
        cardComponentsPage.clickOnCreateButton();
        cardDialogPage = new CardDialogPage();
        expect(cardDialogPage.getModalTitle()).toMatch(/highCouncilApp.card.home.createOrEditLabel/);
        cardDialogPage.close();
    });

    it('should create and save Cards', () => {
        cardComponentsPage.clickOnCreateButton();
        cardDialogPage.setNameInput('name');
        expect(cardDialogPage.getNameInput()).toMatch('name');
        cardDialogPage.setDescriptionInput('description');
        expect(cardDialogPage.getDescriptionInput()).toMatch('description');
        cardDialogPage.setCodeInput('code');
        expect(cardDialogPage.getCodeInput()).toMatch('code');
        cardDialogPage.setNumberInDeckInput('5');
        expect(cardDialogPage.getNumberInDeckInput()).toMatch('5');
        cardDialogPage.save();
        expect(cardDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CardComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-card div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CardDialogPage {
    modalTitle = element(by.css('h4#myCardLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    descriptionInput = element(by.css('input#field_description'));
    codeInput = element(by.css('input#field_code'));
    numberInDeckInput = element(by.css('input#field_numberInDeck'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNameInput = function (name) {
        this.nameInput.sendKeys(name);
    }

    getNameInput = function () {
        return this.nameInput.getAttribute('value');
    }

    setDescriptionInput = function (description) {
        this.descriptionInput.sendKeys(description);
    }

    getDescriptionInput = function () {
        return this.descriptionInput.getAttribute('value');
    }

    setCodeInput = function (code) {
        this.codeInput.sendKeys(code);
    }

    getCodeInput = function () {
        return this.codeInput.getAttribute('value');
    }

    setNumberInDeckInput = function (numberInDeck) {
        this.numberInDeckInput.sendKeys(numberInDeck);
    }

    getNumberInDeckInput = function () {
        return this.numberInDeckInput.getAttribute('value');
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
