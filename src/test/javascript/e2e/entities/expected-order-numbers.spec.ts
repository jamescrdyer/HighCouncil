import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('ExpectedOrderNumbers e2e test', () => {

    let navBarPage: NavBarPage;
    let expectedOrderNumbersDialogPage: ExpectedOrderNumbersDialogPage;
    let expectedOrderNumbersComponentsPage: ExpectedOrderNumbersComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load ExpectedOrderNumbers', () => {
        navBarPage.goToEntity('expected-order-numbers');
        expectedOrderNumbersComponentsPage = new ExpectedOrderNumbersComponentsPage();
        expect(expectedOrderNumbersComponentsPage.getTitle()).toMatch(/highCouncilApp.expectedOrderNumbers.home.title/);

    });

    it('should load create ExpectedOrderNumbers dialog', () => {
        expectedOrderNumbersComponentsPage.clickOnCreateButton();
        expectedOrderNumbersDialogPage = new ExpectedOrderNumbersDialogPage();
        expect(expectedOrderNumbersDialogPage.getModalTitle()).toMatch(/highCouncilApp.expectedOrderNumbers.home.createOrEditLabel/);
        expectedOrderNumbersDialogPage.close();
    });

    it('should create and save ExpectedOrderNumbers', () => {
        expectedOrderNumbersComponentsPage.clickOnCreateButton();
        expectedOrderNumbersDialogPage.setNumberOfPlayersInput('5');
        expect(expectedOrderNumbersDialogPage.getNumberOfPlayersInput()).toMatch('5');
        expectedOrderNumbersDialogPage.setPlayerNumberInput('5');
        expect(expectedOrderNumbersDialogPage.getPlayerNumberInput()).toMatch('5');
        expectedOrderNumbersDialogPage.setOrdersExpectedInput('5');
        expect(expectedOrderNumbersDialogPage.getOrdersExpectedInput()).toMatch('5');
        expectedOrderNumbersDialogPage.save();
        expect(expectedOrderNumbersDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ExpectedOrderNumbersComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-expected-order-numbers div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ExpectedOrderNumbersDialogPage {
    modalTitle = element(by.css('h4#myExpectedOrderNumbersLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    numberOfPlayersInput = element(by.css('input#field_numberOfPlayers'));
    playerNumberInput = element(by.css('input#field_playerNumber'));
    ordersExpectedInput = element(by.css('input#field_ordersExpected'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNumberOfPlayersInput = function (numberOfPlayers) {
        this.numberOfPlayersInput.sendKeys(numberOfPlayers);
    }

    getNumberOfPlayersInput = function () {
        return this.numberOfPlayersInput.getAttribute('value');
    }

    setPlayerNumberInput = function (playerNumber) {
        this.playerNumberInput.sendKeys(playerNumber);
    }

    getPlayerNumberInput = function () {
        return this.playerNumberInput.getAttribute('value');
    }

    setOrdersExpectedInput = function (ordersExpected) {
        this.ordersExpectedInput.sendKeys(ordersExpected);
    }

    getOrdersExpectedInput = function () {
        return this.ordersExpectedInput.getAttribute('value');
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
