import { Pipe, PipeTransform } from '@angular/core';

import { ActionResolution, Action } from '../action-resolution/action-resolution.model';

@Pipe({
    name: 'actionresolutionbyactionfilter',
    pure: false
})
export class ActionResolutionByActionFilter implements PipeTransform {
    transform(actionResolutions: ActionResolution[], filter: Action): any {
        if (!actionResolutions || !filter) {
            return actionResolutions;
        }
        return actionResolutions.filter((item) => item.action === filter);
    }
}
