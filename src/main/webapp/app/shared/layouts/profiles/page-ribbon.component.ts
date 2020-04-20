import { Component, OnInit } from '@angular/core';
import { ProfileService } from 'app/shared/layouts/profiles/profile.service';
import { ProfileInfo } from './profile-info.model';

@Component({
    selector: 'jhi-page-ribbon',
    template: `
        <div class="ribbon" *ngIf="ribbonEnv">
            <a href="" jhiTranslate="global.ribbon.{{ ribbonEnv }}">{{ ribbonEnv }}</a>
        </div>
    `,
    styleUrls: ['page-ribbon.scss'],
})
export class PageRibbonComponent implements OnInit {
    profileInfo: ProfileInfo;
    ribbonEnv: string;

    constructor(private profileService: ProfileService) {}

    ngOnInit() {
        this.profileService.getProfileInfo().subscribe(
            (profileInfo) => {
                if (profileInfo) {
                    this.profileInfo = profileInfo;
                    this.ribbonEnv = profileInfo.ribbonEnv;
                    if (profileInfo.inProduction && window.location.host === 'artemistest.ase.in.tum.de') {
                        this.ribbonEnv = 'test';
                    }
                }
            },
            (reason) => {},
        );
    }
}