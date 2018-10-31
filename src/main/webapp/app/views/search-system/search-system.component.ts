import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ISentence } from 'app/shared/model/sentence.model';
import { LoginService, Principal, Account } from 'app/core';
import { IFilters } from 'app/shared/model/filters.model';

import { ITEMS_PER_PAGE } from 'app/shared';
import { SearchSystemService } from './search-system.service';

@Component({
    selector: 'jhi-search-system',
    templateUrl: './search-system.component.html',
    styleUrls: ['search-system.scss']
  })
  export class SearchSystemComponent implements OnInit, OnDestroy {

    currentAccount: any;
      sentences: ISentence[];
      error: any;
      success: any;
      eventSubscriber: Subscription;
      currentSearch: string;
      routeData: any;
      links: any;
      totalItems: any;
      queryCount: any;
      itemsPerPage: any;
      page: any;
      predicate: any;
      previousPage: any;
      reverse: any;
      filters: IFilters;
      selectedCountry: string;
      selectedGroup: string;
      query: string;
      textQuery: string;
      isCollapsed = {};
      selectedTab = 1;
      selectedId: string;

      constructor(
          private searchSystemService: SearchSystemService,
          private parseLinks: JhiParseLinks,
          private jhiAlertService: JhiAlertService,
          private principal: Principal,
          private loginService: LoginService,
          private activatedRoute: ActivatedRoute,
          private router: Router,
          private eventManager: JhiEventManager
      ) {
          this.itemsPerPage = ITEMS_PER_PAGE;
          this.routeData = this.activatedRoute.data.subscribe(data => {
              this.page = data.pagingParams.page;
              this.previousPage = data.pagingParams.page;
              this.reverse = data.pagingParams.ascending;
              this.predicate = data.pagingParams.predicate;
          });
          this.currentSearch =
              this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                  ? this.activatedRoute.snapshot.params['search']
                  : '';
      }

      login() {
        this.loginService.login();
    }

      loadAll() {
          if (this.currentSearch) {
              this.searchSystemService
                  .search({
                      page: this.page - 1,
                      query: this.currentSearch,
                      size: this.itemsPerPage,
                      sort: this.sort()
                  })
                  .subscribe(
                      (res: HttpResponse<ISentence[]>) => this.paginateSentences(res.body, res.headers),
                      (res: HttpErrorResponse) => this.onError(res.message)
                  );
              return;
          }
          this.searchSystemService
              .query({
                  page: this.page - 1,
                  size: this.itemsPerPage,
                  sort: this.sort()
              })
              .subscribe(
                  (res: HttpResponse<ISentence[]>) => this.paginateSentences(res.body, res.headers),
                  (res: HttpErrorResponse) => this.onError(res.message)
              );
      }

      loadPage(page: number) {
          if (page !== this.previousPage) {
              this.previousPage = page;
              this.transition();
          }
      }

      transition() {
          this.router.navigate([''], {
              queryParams: {
                  page: this.page,
                  size: this.itemsPerPage,
                  search: this.currentSearch,
                  sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
              }
          });
          this.loadAll();
      }

      clear() {
          this.page = 0;
          this.textQuery = '';
          this.router.navigate([
              '',
              {
                  page: this.page,
                  sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
              }
          ]);
          this.buildSearch('');
          this.loadAll();
      }

      search(query) {
          if (!query) {
              return this.clear();
          }
          this.page = 0;
          this.buildSearch(query);
          this.router.navigate([
              '',
              {
                  search: this.currentSearch,
                  page: this.page,
                  sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
              }
          ]);
          this.loadAll();
      }

      getFilters() {
          this.searchSystemService.getFilters().subscribe(
              (res: HttpResponse<IFilters>) => this.onSuccess(res.body, res.headers),
              (res: HttpErrorResponse) => this.onErrors(res.message),
          );
      }

      onSuccess(data: any, headers: any): any {
          this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
          this.queryCount = this.totalItems;

          this.filters = data;
      }

      onErrors(res: any): any {
          console.log(res);
      }

      ngOnInit() {
         // this.loadAll();
          this.principal.identity().then(account => {
              this.currentAccount = account;
          });
          this.registerChangeInSentences();
          this.getFilters();
      }

      ngOnDestroy() {
          this.eventManager.destroy(this.eventSubscriber);
      }

      trackId(index: number, item: ISentence) {
          return item.id;
      }

      registerChangeInSentences() {
          this.eventSubscriber = this.eventManager.subscribe('sentenceListModification', response => this.loadAll());
      }

      sort() {
          const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
          if (this.predicate !== 'id') {
              result.push('id');
          }
          return result;
      }

      private paginateSentences(data: ISentence[], headers: HttpHeaders) {
          this.links = this.parseLinks.parse(headers.get('link'));
          this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
          this.queryCount = this.totalItems;
          this.sentences = this.searchProcess(data);
      }

      private onError(errorMessage: string) {
          this.jhiAlertService.error(errorMessage, null, null);
      }

      setCountry(country: string) {
          if (this.selectedCountry === country) {
              this.selectedCountry = '';
              this.currentSearch = '';
          } else {
              this.selectedCountry = country;
          }
          this.buildSearch('');
          this.updateSearch();
      }

      setGroup(group: string) {
          if (this.selectedGroup === group) {
              this.selectedGroup = '';
              this.currentSearch = '';
          } else {
              this.selectedGroup = group;
          }
          this.buildSearch('');
          this.updateSearch();
      }

      setItem(item: string) {
          this.selectedId = item;
          console.log(this.selectedId);
      }

      setTab(tab: number) {
          this.selectedTab = tab;
      }

      private updateSearch() {
          this.router.navigate([
              '',
              {
                  search: this.currentSearch,
                  page: this.page,
                  sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
              }
          ]);
          this.loadAll();
      }

      buildSearch(query) {
          this.currentSearch = '';

          if (query !== undefined && query != null && query.trim().length > 0) {
              this.currentSearch = '"' + query + '"';
          }
          if (this.selectedGroup !== undefined && this.selectedGroup != null && this.selectedGroup.trim().length > 0) {
              this.currentSearch = (this.currentSearch.trim().length > 0 ? this.currentSearch + ' AND ' : '') + 'group.equals="' + this.selectedGroup + '"';
          }
          if (this.selectedCountry !== undefined && this.selectedCountry != null && this.selectedCountry.trim().length > 0) {
              this.currentSearch = (this.currentSearch.trim().length > 0 ? this.currentSearch + ' AND ' : '')  + 'country.equals=\'' + this.selectedCountry + '\'';
          }
      }

      searchProcess(data: ISentence[]) {
          data.forEach(element => {
              element.title = this.highlight(element.title, this.textQuery);
              element.country = this.highlight(element.country, this.textQuery);
              element.status = this.highlight(element.status, this.textQuery);
              element.group = this.highlight(element.group, this.textQuery);
              element.emisor = this.highlight(element.emisor, this.textQuery);
              element.facts = this.highlight(element.facts, this.textQuery);
              element.argumentsSummary = this.highlight(element.argumentsSummary, this.textQuery);
          });

          return data;
      }

      highlight(text: string, search: string) {
              if (search && text) {
                let pattern = search.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, '\\$&');
                pattern = pattern.split(' ').filter(t => {
                  return t.length > 0;
                }).join('|');
                const regex = new RegExp(pattern, 'gi');
                return text.replace(regex, match => `<span class="search-highlight">${match}</span>`);
              } else {
                return text;
              }
      }
}
