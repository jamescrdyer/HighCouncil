import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('PlayerTurnResult e2e test', () => {

    let navBarPage: NavBarPage;
    let playerTurnResultDialogPage: PlayerTurnResultDialogPage;
    let playerTurnResultComponentsPage: PlayerTurnResultComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load PlayerTurnResults', () => {
        navBarPage.goToEntity('player-turn-result');
        playerTurnResultComponentsPage = new PlayerTurnResultComponentsPage();
        expect(playerTurnResultComponentsPage.getTitle()).toMatch(/highCouncilApp.playerTurnResult.home.title/);

    });

    it('should load create PlayerTurnResult dialog', () => {
        playerTurnResultComponentsPage.clickOnCreateButton();
        playerTurnResultDialogPage = new PlayerTurnResultDialogPage();
        expect(playerTurnResultDialogPage.getModalTitle()).toMatch(/highCouncilApp.playerTurnResult.home.createOrEditLabel/);
        playerTurnResultDialogPage.close();
    });

    it('should create and save PlayerTurnResults', () => {
        playerTurnResultComponentsPage.clickOnCreateButton();
        playerTurnResultDialogPage.setPietyInput('5');
        expect(playerTurnResultDialogPage.getPietyInput()).toMatch('5');
        playerTurnResultDialogPage.setPopularityInput('5');
        expect(playerTurnResultDialogPage.getPopularityInput()).toMatch('5');
        playerTurnResultDialogPage.setMilitaryInput('5');
        expect(playerTurnResultDialogPage.getMilitaryInput()).toMatch('5');
        playerTurnResultDialogPage.setWealthInput('5');
        expect(playerTurnResultDialogPage.getWealthInput()).toMatch('5');
        playerTurnResultDialogPage.setFavourInput('5');
        expect(playerTurnResultDialogPage.getFavourInput()).toMatch('5');
        playerTurnResultDialogPage.setPenaltyInput('5');
        expect(playerTurnResultDialogPage.getPenaltyInput()).toMatch('5');
        playerTurnResultDialogPage.setTurnInput('5');
        expect(playerTurnResultDialogPage.getTurnInput()).toMatch('5');
        playerTurnResultDialogPage.actionSelectLastOption();
        playerTurnResultDialogPage.gameSelectLastOption();
        playerTurnResultDialogPage.playerSelectLastOption();
        playerTurnResultDialogPage.turnResultSelectLastOption();
        playerTurnResultDialogPage.save();
        expect(playerTurnResultDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class PlayerTurnResultComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-player-turn-result div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class PlayerTurnResultDialogPage {
    modalTitle = element(by.css('h4#myPlayerTurnResultLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    pietyInput = element(by.css('input#field_piety'));
    popularityInput = element(by.css('input#field_popularity'));
    militaryInput = element(by.css('input#field_military'));
    wealthInput = element(by.css('input#field_wealth'));
    favourInput = element(by.css('input#field_favour'));
    penaltyInput = element(by.css('input#field_penalty'));
    turnInput = element(by.css('input#field_turn'));
    actionSelect = element(by.css('select#field_action'));
    gameSelect = element(by.css('select#field_game'));
    playerSelect = element(by.css('select#field_player'));
    turnResultSelect = element(by.css('select#field_turnResult'));

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

    setPenaltyInput = function (penalty) {
        this.penaltyInput.sendKeys(penalty);
    }

    getPenaltyInput = function () {
        return this.penaltyInput.getAttribute('value');
    }

    setTurnInput = function (turn) {
        this.turnInput.sendKeys(turn);
    }

    getTurnInput = function () {
        return this.turnInput.getAttribute('value');
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

    playerSelectLastOption = function () {
        this.playerSelect.all(by.tagName('option')).last().click();
    }

    playerSelectOption = function (option) {
        this.playerSelect.sendKeys(option);
    }

    getPlayerSelect = function () {
        return this.playerSelect;
    }

    getPlayerSelectedOption = function () {
        return this.playerSelect.element(by.css('option:checked')).getText();
    }

    turnResultSelectLastOption = function () {
        this.turnResultSelect.all(by.tagName('option')).last().click();
    }

    turnResultSelectOption = function (option) {
        this.turnResultSelect.sendKeys(option);
    }

    getTurnResultSelect = function () {
        return this.turnResultSelect;
    }

    getTurnResultSelectedOption = function () {
        return this.turnResultSelect.element(by.css('option:checked')).getText();
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
