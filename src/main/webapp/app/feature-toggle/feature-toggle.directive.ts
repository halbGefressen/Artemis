import { Directive, HostBinding, Input, OnDestroy, OnInit } from '@angular/core';
import { FeatureToggle, FeatureToggleService } from 'app/feature-toggle/feature-toggle.service';
import { tap } from 'rxjs/operators';
import { Subscription } from 'rxjs';

@Directive({
    selector: '[jhiFeatureToggle]',
})
export class FeatureToggleDirective implements OnInit, OnDestroy {
    @Input('jhiFeatureToggle') feature: FeatureToggle;
    /**
     * This input must be used to overwrite the disabled state given that the feature toggle is inactive.
     * If the normal [disabled] directive of Angular would be used, the HostBinding in this directive would always enable the element if the feature is active.
     */
    @Input() overwriteDisabled: boolean | null;
    /**
     * Condition to check even before checking for the feature toggle. If true, the feature toggle won't get checked.
     * This can be useful e.g. if you use the same button for different features (like our delete button) and only want
     * to check the toggle for programming exercises
     */
    @Input() skipFeatureToggle: boolean;
    private featureActive = true;

    private featureToggleActiveSubscription: Subscription;

    constructor(private featureToggleService: FeatureToggleService) {}

    ngOnInit() {
        // If no feature is set for the toggle, the directive does nothing.
        if (this.feature) {
            this.featureToggleActiveSubscription = this.featureToggleService
                .getFeatureToggleActive(this.feature)
                .pipe(
                    // Disable the element if the feature is inactive.
                    tap(active => {
                        this.featureActive = this.skipFeatureToggle || active;
                    }),
                )
                .subscribe();
        }
    }

    ngOnDestroy(): void {
        if (this.featureToggleActiveSubscription) {
            this.featureToggleActiveSubscription.unsubscribe();
        }
    }

    /**
     * This will disable the feature component (normally a button) if the specified feature flag is inactive OR
     * if there is some other condition given as an Input, which takes higher priority (input overwriteDisabled)
     */
    @HostBinding('disabled')
    get disabled(): boolean {
        return this.overwriteDisabled || !this.featureActive;
    }
}