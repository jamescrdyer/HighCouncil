import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Player e2e test', () => {

    let navBarPage: NavBarPage;
    let playerDialogPage: PlayerDialogPage;
    let playerComponentsPage: PlayerComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Players', () => {
        navBarPage.goToEntity('player');
        playerComponentsPage = new PlayerComponentsPage();
        expect(playerComponentsPage.getTitle()).toMatch(/highCouncilApp.player.home.title/);

    });

    it('should load create Player dialog', () => {
        playerComponentsPage.clickOnCreateButton();
        playerDialogPage = new PlayerDialogPage();
        expect(playerDialogPage.getModalTitle()).toMatch(/highCouncilApp.player.home.createOrEditLabel/);
        playerDialogPage.close();
    });

    it('should create and save Players', () => {
        playerComponentsPage.clickOnCreateButton();
        playerDialogPage.setPietyInput('5');
        expect(playerDialogPage.getPietyInput()).toMatch('5');
        playerDialogPage.setPopularityInput('5');
        expect(playerDialogPage.getPopularityInput()).toMatch('5');
        playerDialogPage.setMilitaryInput('5');
        expect(playerDialogPage.getMilitaryInput()).toMatch('5');
        playerDialogPage.setWealthInput('5');
        expect(playerDialogPage.getWealthInput()).toMatch('5');
        playerDialogPage.setFavourInput('5');
        expect(playerDialogPage.getFavourInput()).toMatch('5');
        playerDialogPage.getChancellorInput().isSelected().then(function (selected) {
            if (selected) {
                playerDialogPage.getChancellorInput().click();
                expect(playerDialogPage.getChancellorInput().isSelected()).toBeFalsy();
            } else {
                playerDialogPage.getChancellorInput().click();
                expect(playerDialogPage.getChancellorInput().isSelected()).toBeTruthy();
            }
        });
        playerDialogPage.setNameInput('name');
        expect(playerDialogPage.getNameInput()).toMatch('name');
        playerDialogPage.getPhaseLockedInput().isSelected().then(function (selected) {
            if (selected) {
                playerDialogPage.getPhaseLockedInput().click();
                expect(playerDialogPage.getPhaseLockedInput().isSelected()).toBeFalsy();
            } else {
                playerDialogPage.getPhaseLockedInput().click();
                expect(playerDialogPage.getPhaseLockedInput().isSelected()).toBeTruthy();
            }
        });
        playerDialogPage.gameSelectLastOption();
        playerDialogPage.userSelectLastOption();
        // playerDialogPage.handSelectLastOption();
        playerDialogPage.save();
        expect(playerDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class PlayerComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-player div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class PlayerDialogPage {
    modalTitle = element(by.css('h4#myPlayerLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    pietyInput = element(by.css('input#field_piety'));
    popularityInput = element(by.css('input#field_popularity'));
    militaryInput = element(by.css('input#field_military'));
    wealthInput = element(by.css('input#field_wealth'));
    favourInput = element(by.css('input#field_favour'));
    chancellorInput = element(by.css('input#field_chancellor'));
    nameInput = element(by.css('input#field_name'));
    phaseLockedInput = element(by.css('input#field_phaseLocked'));
    gameSelect = element(by.css('select#field_game'));
    userSelect = element(by.css('select#field_user'));
    handSelect = element(by.css('select#field_hand'));

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

    getChancellorInput = function () {
        return this.chancellorInput;
    }
    setNameInput = function (name) {
        this.nameInput.sendKeys(name);
    }

    getNameInput = function () {
        return this.nameInput.getAttribute('value');
    }

    getPhaseLockedInput = function () {
        return this.phaseLockedInput;
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

    userSelectLastOption = function () {
        this.userSelect.all(by.tagName('option')).last().click();
    }

    userSelectOption = function (option) {
        this.userSelect.sendKeys(option);
    }

    getUserSelect = function () {
        return this.userSelect;
    }

    getUserSelectedOption = function () {
        return this.userSelect.element(by.css('option:checked')).getText();
    }

    handSelectLastOption = function () {
        this.handSelect.all(by.tagName('option')).last().click();
    }

    handSelectOption = function (option) {
        this.handSelect.sendKeys(option);
    }

    getHandSelect = function () {
        return this.handSelect;
    }

    getHandSelectedOption = function () {
        return this.handSelect.element(by.css('option:checked')).getText();
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
