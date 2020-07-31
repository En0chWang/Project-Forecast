import { Component, OnInit, Output, EventEmitter, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LOCAL_STORAGE } from '@ng-toolkit/universal';
@Component({
  selector: 'app-favorites',
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.css']
})
export class FavoritesComponent implements OnInit {


  @Output() messageEvent = new EventEmitter<any>();

  constructor(@Inject(LOCAL_STORAGE) private localStorage: any, public http: HttpClient) {
  }

  public favorites: { index: number, image: string, city: string, state: string }[] = new Array(this.localStorage.length);
  public hasRecords: boolean;
  getFavorties() {
    for (var i = 0; i < this.localStorage.length; i++) {
      let key = this.localStorage.key(i);
      let value = JSON.parse(this.localStorage.getItem(key));
      this.favorites[i] = {
        index: i + 1,
        image: value.image,
        city: key,
        state: value.state
      }
    }
  }
  deleteItem(city: string) {
    this.localStorage.removeItem(city);
    var child = document.getElementById(city);
    child.parentNode.removeChild(child);
    if (this.localStorage.length == 0) {
      this.hasRecords = false;
    }
  }
  ngOnInit() {

    if (this.localStorage.length > 0) {
      this.hasRecords = true;
      this.getFavorties();
    } else {
      this.hasRecords = false;
    }

  }

  getData(city: string) {
    console.log(city);
    console.log('gete data');
    let value = JSON.parse(this.localStorage.getItem(city));
    let req = { city: city, value: value };
    this.messageEvent.emit(req);
  }

}
