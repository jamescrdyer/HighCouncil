import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Game e2e test', () => {

    let navBarPage: NavBarPage;
    let gameDialogPage: GameDialogPage;
    let gameComponentsPage: GameComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Games', () => {
        navBarPage.goToEntity('game');
        gameComponentsPage = new GameComponentsPage();
        expect(gameComponentsPage.getTitle()).toMatch(/highCouncilApp.game.home.title/);

    });

    it('should load create Game dialog', () => {
        gameComponentsPage.clickOnCreateButton();
        gameDialogPage = new GameDialogPage();
        expect(gameDialogPage.getModalTitle()).toMatch(/highCouncilApp.game.home.createOrEditLabel/);
        gameDialogPage.close();
    });

    it('should create and save Games', () => {
        gameComponentsPage.clickOnCreateButton();
        gameDialogPage.setTimeLimitSecondsInput('5');
        expect(gameDialogPage.getTimeLimitSecondsInput()).toMatch('5');
        gameDialogPage.phaseSelectLastOption();
        gameDialogPage.setTurnInput('5');
        expect(gameDialogPage.getTurnInput()).toMatch('5');
        gameDialogPage.kingdomSelectLastOption();
        gameDialogPage.deckSelectLastOption();
        gameDialogPage.save();
        expect(gameDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class GameComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-game div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class GameDialogPage {
    modalTitle = element(by.css('h4#myGameLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    timeLimitSecondsInput = element(by.css('input#field_timeLimitSeconds'));
    phaseSelect = element(by.css('select#field_phase'));
    turnInput = element(by.css('input#field_turn'));
    kingdomSelect = element(by.css('select#field_kingdom'));
    deckSelect = element(by.css('select#field_deck'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setTimeLimitSecondsInput = function (timeLimitSeconds) {
        this.timeLimitSecondsInput.sendKeys(timeLimitSeconds);
    }

    getTimeLimitSecondsInput = function () {
        return this.timeLimitSecondsInput.getAttribute('value');
    }

    setPhaseSelect = function (phase) {
        this.phaseSelect.sendKeys(phase);
    }

    getPhaseSelect = function () {
        return this.phaseSelect.element(by.css('option:checked')).getText();
    }

    phaseSelectLastOption = function () {
        this.phaseSelect.all(by.tagName('option')).last().click();
    }
    setTurnInput = function (turn) {
        this.turnInput.sendKeys(turn);
    }

    getTurnInput = function () {
        return this.turnInput.getAttribute('value');
    }

    kingdomSelectLastOption = function () {
        this.kingdomSelect.all(by.tagName('option')).last().click();
    }

    kingdomSelectOption = function (option) {
        this.kingdomSelect.sendKeys(option);
    }

    getKingdomSelect = function () {
        return this.kingdomSelect;
    }

    getKingdomSelectedOption = function () {
        return this.kingdomSelect.element(by.css('option:checked')).getText();
    }

    deckSelectLastOption = function () {
        this.deckSelect.all(by.tagName('option')).last().click();
    }

    deckSelectOption = function (option) {
        this.deckSelect.sendKeys(option);
    }

    getDeckSelect = function () {
        return this.deckSelect;
    }

    getDeckSelectedOption = function () {
        return this.deckSelect.element(by.css('option:checked')).getText();
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
