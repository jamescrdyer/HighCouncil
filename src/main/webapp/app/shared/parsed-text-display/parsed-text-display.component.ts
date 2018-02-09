import { Component, OnChanges, OnInit, Input } from '@angular/core';

export enum Action {
    'Piety' = 'Piety',
    'Popularity' = 'Popularity',
    'Favour' = 'Favour',
    'Military' = 'Military',
    'Wealth' = 'Wealth'
}

@Component({
    selector: 'jhi-parsed-text-display',
    styleUrls: [ 'parsed-text-display.component.scss' ],
    template: `
        <div class="parsedText">
            <span *ngFor="let token of tokens" [ngClass]="token.action">{{token.value}} </span>
        </div>`
})
export class JhiParsedTextDisplayComponent implements OnInit, OnChanges {
    public tokens: any[];
    @Input() text: string;

    constructor() { }

    ngOnInit() {
        this.parseText();
    }

    ngOnChanges() {
        this.parseText();
    }

    private parseText() {
        this.tokens = [];
        if (this.text && this.text.length > 0) {
            const splitTokens = this.text.split(' ');
            let token = this.createToken();
            for (const splitToken of splitTokens) {
                if (splitToken in Action) {
                    token.action = splitToken;
                } else {
                    token.value = splitToken;
                    this.tokens.push(token);
                    token = this.createToken();
                }
            }
        }
    }
    private createToken(): any {
        return {
          action: null,
          value: null
        };
    }
}
