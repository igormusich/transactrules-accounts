import { ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { SidenavItem } from '../../sidenav/sidenav-item/sidenav-item.model';
import { NavigationEnd, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import * as fromRoot from '../../../reducers/index';
import filter from 'lodash-es/filter';
import find from 'lodash-es/find';
import each from 'lodash-es/each';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/fromEvent';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/filter';

@Component({
  selector: 'vr-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.scss']
})
export class SearchBarComponent implements OnInit {

  input: string;

  @ViewChild('inputElem') inputElem: ElementRef;
  focused: boolean;

  recentlyVisited: SidenavItem[] = [ ];
  frequentlyVisited: SidenavItem[] = [ ];
  sidenavItems: SidenavItem[] = [ ];
  searchResult: SidenavItem[] = [ ];

  constructor(
    private router: Router,
    private store: Store<fromRoot.State>,
    private cd: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.store.select(fromRoot.getSidenavItems).filter(Boolean).subscribe((items) => {
      this.sidenavItems = items;
      this.cd.markForCheck();
    });

    Observable.fromEvent(this.inputElem.nativeElement, 'keyup')
      .distinctUntilChanged()
      .subscribe(() => {
        if (this.inputElem.nativeElement.value !== '') {
          this.searchResult = filter(this.sidenavItems, (item) => {
            return item.name.toLowerCase().includes(this.inputElem.nativeElement.value)
          });

          this.cd.markForCheck();
        }
      });

    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {

        const item = this.findByRouteRecursive(event.urlAfterRedirects);

        const index = this.recentlyVisited.indexOf(item);
        if (index > -1) {
          this.recentlyVisited.splice(index, 1);
        }

        this.recentlyVisited.unshift(item);

        if (this.recentlyVisited.length > 5) {
          this.recentlyVisited.pop();
        }

        this.cd.markForCheck();
      }

    });
  }

  findByRouteRecursive(route: string, collection: SidenavItem[] = this.sidenavItems) {
    let result = find(collection, { 'route': route });

    if (!result) {
      each(collection, (item) => {
        if (item.hasSubItems()) {
          const found = this.findByRouteRecursive(route, item.subItems);

          if (found) {
            result = found;
            return false;
          }
        }
      });
    }

    return result;
  }

  openDropdown() {
    this.focused = true;
  }

  closeDropdown() {
    this.focused = false;
  }
}
