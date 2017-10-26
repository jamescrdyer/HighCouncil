import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';


describe('Deck e2e test', () => {

    let navBarPage: NavBarPage;
    let deckDialogPage: DeckDialogPage;
    let deckComponentsPage: DeckComponentsPage;


    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Decks', () => {
        navBarPage.goToEntity('deck');
        deckComponentsPage = new DeckComponentsPage();
        expect(deckComponentsPage.getTitle()).toMatch(/highCouncilApp.deck.home.title/);

    });

    it('should load create Deck dialog', () => {
        deckComponentsPage.clickOnCreateButton();
        deckDialogPage = new DeckDialogPage();
        expect(deckDialogPage.getModalTitle()).toMatch(/highCouncilApp.deck.home.createOrEditLabel/);
        deckDialogPage.close();
    });

    it('should create and save Decks', () => {
        deckComponentsPage.clickOnCreateButton();
        // deckDialogPage.cardSelectLastOption();
        // deckDialogPage.discardSelectLastOption();
        deckDialogPage.save();
        expect(deckDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class DeckComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-deck div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class DeckDialogPage {
    modalTitle = element(by.css('h4#myDeckLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    cardSelect = element(by.css('select#field_card'));
    discardSelect = element(by.css('select#field_discard'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    cardSelectLastOption = function () {
        this.cardSelect.all(by.tagName('option')).last().click();
    }

    cardSelectOption = function (option) {
        this.cardSelect.sendKeys(option);
    }

    getCardSelect = function () {
        return this.cardSelect;
    }

    getCardSelectedOption = function () {
        return this.cardSelect.element(by.css('option:checked')).getText();
    }

    discardSelectLastOption = function () {
        this.discardSelect.all(by.tagName('option')).last().click();
    }

    discardSelectOption = function (option) {
        this.discardSelect.sendKeys(option);
    }

    getDiscardSelect = function () {
        return this.discardSelect;
    }

    getDiscardSelectedOption = function () {
        return this.discardSelect.element(by.css('option:checked')).getText();
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
