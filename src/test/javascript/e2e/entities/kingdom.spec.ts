import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Kingdom e2e test', () => {

    let navBarPage: NavBarPage;
    let kingdomDialogPage: KingdomDialogPage;
    let kingdomComponentsPage: KingdomComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Kingdoms', () => {
        navBarPage.goToEntity('kingdom');
        kingdomComponentsPage = new KingdomComponentsPage();
        expect(kingdomComponentsPage.getTitle()).toMatch(/highCouncilApp.kingdom.home.title/);

    });

    it('should load create Kingdom dialog', () => {
        kingdomComponentsPage.clickOnCreateButton();
        kingdomDialogPage = new KingdomDialogPage();
        expect(kingdomDialogPage.getModalTitle()).toMatch(/highCouncilApp.kingdom.home.createOrEditLabel/);
        kingdomDialogPage.close();
    });

    it('should create and save Kingdoms', () => {
        kingdomComponentsPage.clickOnCreateButton();
        kingdomDialogPage.setPietyInput('5');
        expect(kingdomDialogPage.getPietyInput()).toMatch('5');
        kingdomDialogPage.setPopularityInput('5');
        expect(kingdomDialogPage.getPopularityInput()).toMatch('5');
        kingdomDialogPage.setMilitaryInput('5');
        expect(kingdomDialogPage.getMilitaryInput()).toMatch('5');
        kingdomDialogPage.setWealthInput('5');
        expect(kingdomDialogPage.getWealthInput()).toMatch('5');
        kingdomDialogPage.setHealthInput('5');
        expect(kingdomDialogPage.getHealthInput()).toMatch('5');
        kingdomDialogPage.save();
        expect(kingdomDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class KingdomComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-kingdom div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class KingdomDialogPage {
    modalTitle = element(by.css('h4#myKingdomLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    pietyInput = element(by.css('input#field_piety'));
    popularityInput = element(by.css('input#field_popularity'));
    militaryInput = element(by.css('input#field_military'));
    wealthInput = element(by.css('input#field_wealth'));
    healthInput = element(by.css('input#field_health'));

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

    setHealthInput = function (health) {
        this.healthInput.sendKeys(health);
    }

    getHealthInput = function () {
        return this.healthInput.getAttribute('value');
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
