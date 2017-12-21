import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Orders e2e test', () => {

    let navBarPage: NavBarPage;
    let ordersDialogPage: OrdersDialogPage;
    let ordersComponentsPage: OrdersComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Orders', () => {
        navBarPage.goToEntity('orders');
        ordersComponentsPage = new OrdersComponentsPage();
        expect(ordersComponentsPage.getTitle()).toMatch(/highCouncilApp.orders.home.title/);

    });

    it('should load create Orders dialog', () => {
        ordersComponentsPage.clickOnCreateButton();
        ordersDialogPage = new OrdersDialogPage();
        expect(ordersDialogPage.getModalTitle()).toMatch(/highCouncilApp.orders.home.createOrEditLabel/);
        ordersDialogPage.close();
    });

    it('should create and save Orders', () => {
        ordersComponentsPage.clickOnCreateButton();
        ordersDialogPage.setTurnInput('5');
        expect(ordersDialogPage.getTurnInput()).toMatch('5');
        ordersDialogPage.setPietyInput('5');
        expect(ordersDialogPage.getPietyInput()).toMatch('5');
        ordersDialogPage.setPopularityInput('5');
        expect(ordersDialogPage.getPopularityInput()).toMatch('5');
        ordersDialogPage.setMilitaryInput('5');
        expect(ordersDialogPage.getMilitaryInput()).toMatch('5');
        ordersDialogPage.setWealthInput('5');
        expect(ordersDialogPage.getWealthInput()).toMatch('5');
        ordersDialogPage.setFavourInput('5');
        expect(ordersDialogPage.getFavourInput()).toMatch('5');
        ordersDialogPage.actionSelectLastOption();
        ordersDialogPage.gameSelectLastOption();
        ordersDialogPage.playerSelectLastOption();
        ordersDialogPage.save();
        expect(ordersDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class OrdersComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-orders div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class OrdersDialogPage {
    modalTitle = element(by.css('h4#myOrdersLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    turnInput = element(by.css('input#field_turn'));
    pietyInput = element(by.css('input#field_piety'));
    popularityInput = element(by.css('input#field_popularity'));
    militaryInput = element(by.css('input#field_military'));
    wealthInput = element(by.css('input#field_wealth'));
    favourInput = element(by.css('input#field_favour'));
    actionSelect = element(by.css('select#field_action'));
    gameSelect = element(by.css('select#field_game'));
    playerSelect = element(by.css('select#field_player'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setTurnInput = function (turn) {
        this.turnInput.sendKeys(turn);
    }

    getTurnInput = function () {
        return this.turnInput.getAttribute('value');
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
