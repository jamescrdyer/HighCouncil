import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('TurnResult e2e test', () => {

    let navBarPage: NavBarPage;
    let turnResultDialogPage: TurnResultDialogPage;
    let turnResultComponentsPage: TurnResultComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load TurnResults', () => {
        navBarPage.goToEntity('turn-result');
        turnResultComponentsPage = new TurnResultComponentsPage();
        expect(turnResultComponentsPage.getTitle()).toMatch(/highCouncilApp.turnResult.home.title/);

    });

    it('should load create TurnResult dialog', () => {
        turnResultComponentsPage.clickOnCreateButton();
        turnResultDialogPage = new TurnResultDialogPage();
        expect(turnResultDialogPage.getModalTitle()).toMatch(/highCouncilApp.turnResult.home.createOrEditLabel/);
        turnResultDialogPage.close();
    });

    it('should create and save TurnResults', () => {
        turnResultComponentsPage.clickOnCreateButton();
        turnResultDialogPage.setPietyInput('5');
        expect(turnResultDialogPage.getPietyInput()).toMatch('5');
        turnResultDialogPage.setPopularityInput('5');
        expect(turnResultDialogPage.getPopularityInput()).toMatch('5');
        turnResultDialogPage.setMilitaryInput('5');
        expect(turnResultDialogPage.getMilitaryInput()).toMatch('5');
        turnResultDialogPage.setWealthInput('5');
        expect(turnResultDialogPage.getWealthInput()).toMatch('5');
        turnResultDialogPage.setFavourInput('5');
        expect(turnResultDialogPage.getFavourInput()).toMatch('5');
        turnResultDialogPage.setTurnInput('5');
        expect(turnResultDialogPage.getTurnInput()).toMatch('5');
        turnResultDialogPage.gameSelectLastOption();
        turnResultDialogPage.save();
        expect(turnResultDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class TurnResultComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-turn-result div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class TurnResultDialogPage {
    modalTitle = element(by.css('h4#myTurnResultLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    pietyInput = element(by.css('input#field_piety'));
    popularityInput = element(by.css('input#field_popularity'));
    militaryInput = element(by.css('input#field_military'));
    wealthInput = element(by.css('input#field_wealth'));
    favourInput = element(by.css('input#field_favour'));
    turnInput = element(by.css('input#field_turn'));
    gameSelect = element(by.css('select#field_game'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setPietyInput = function (piety) {
        this.pietyInput.sendKeys(piety);
    }

    getPietyInput = function () {
        return this.pietyInput.getAttribute('value');
    }

    setPopularityInput = function (popularity) {
        this.popularityInput.sendKeys(popularity);
    }

    getPopularityInput = function () {
        return this.popularityInput.getAttribute('value');
    }

    setMilitaryInput = function (military) {
        this.militaryInput.sendKeys(military);
    }

    getMilitaryInput = function () {
        return this.militaryInput.getAttribute('value');
    }

    setWealthInput = function (wealth) {
        this.wealthInput.sendKeys(wealth);
    }

    getWealthInput = function () {
        return this.wealthInput.getAttribute('value');
    }

    setFavourInput = function (favour) {
        this.favourInput.sendKeys(favour);
    }

    getFavourInput = function () {
        return this.favourInput.getAttribute('value');
    }

    setTurnInput = function (turn) {
        this.turnInput.sendKeys(turn);
    }

    getTurnInput = function () {
        return this.turnInput.getAttribute('value');
    }

    gameSelectLastOption = function () {
        this.gameSelect.all(by.tagName('option')).last().click();
    }

    gameSelectOption = function (option) {
        this.gameSelect.sendKeys(option);
    }

    getGameSelect = function () {
        return this.gameSelect;
    }

    getGameSelectedOption = function () {
        return this.gameSelect.element(by.css('option:checked')).getText();
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
