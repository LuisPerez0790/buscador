import { Component, OnInit } from '@angular/core';
import { Principal } from 'app/core';
import { JhiEventManager } from 'ng-jhipster';

import { navAdminItems, navCtranspItems } from 'app/layouts/sidebar/_nav';

@Component({
    selector: 'jhi-sidebar',
    templateUrl: './sidebar.component.html',
    styleUrls: ['sidebar.scss']
})
export class SidebarComponent implements OnInit {
    public navItems: any[] = [];
    public sidebarMinimized = false;
    private changes: MutationObserver;
    public element: HTMLElement = document.body;

    isDisplay: any = false;
    account: Account;

    constructor(private principal: Principal, private eventManager: JhiEventManager) {
        this.changes = new MutationObserver(mutations => {
            this.sidebarMinimized = document.body.classList.contains('sidebar-minimized');
        });

        this.changes.observe(<Element>this.element, {
            attributes: true
        });
    }

    ngOnInit() {
        console.log('init');
        this.principal.identity().then(account => {
            this.account = account;
            if (account) {
                this.initializeMenu();
            } else {
                this.isDisplay = false;
            }
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.account = account;
                this.initializeMenu();
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    initializeMenu() {
        this.isDisplay = 'lg';
        if (this.principal.hasAnyAuthorityDirect(['ROLE_ADMIN'])) {
            this.navItems = [...navAdminItems, ...navCtranspItems];
        }
        if (this.principal.hasAnyAuthorityDirect(['ROLE_CTRANSP_USER'])) {
            this.navItems = [...navCtranspItems];
        }
    }
}
