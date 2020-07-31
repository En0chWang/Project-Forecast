import { Component, OnInit, Input, OnChanges, Inject } from '@angular/core';
import { stringify } from 'querystring';
import { LOCAL_STORAGE } from '@ng-toolkit/universal';

@Component({
  selector: 'app-weathertap',
  templateUrl: './weathertap.component.html',
  styleUrls: ['./weathertap.component.css']
})
export class WeathertapComponent implements OnInit, OnChanges {

  @Input('msg') msg: any;
  @Input('city') city: string;
  @Input('state') state: string;
  public tabValue: string = 'currently';
  public tweetFeed: string;
  public star: boolean = false;
  public star_border: boolean = true;
  constructor(@Inject(LOCAL_STORAGE) private localStorage: any, ) {
  }

  ngOnInit() {
  }

  switchTab(value: string) {
    this.tabValue = value;
  }

  // save in local storage
  addToFavorite() {
    this.star = true;
    this.star_border = false;
    var msg = JSON.parse(this.msg);
    var obj = {
      lat: msg.weather.latitude,
      lon: msg.weather.longitude,
      state: msg.state,
      image: msg.seal.items[0].link
    }
    this.localStorage.setItem(msg.city, JSON.stringify(obj));
  }

  // remove from the local storage
  removeFromFavorite() {
    var msg = JSON.parse(this.msg);
    this.star = false;
    this.star_border = true;
    this.localStorage.removeItem(msg.city);
  }

  ngOnChanges() {
    let temp = JSON.parse(this.msg);
    console.log(temp);
    this.tweetFeed = 'https://twitter.com/intent/tweet?text=' + 'The current temperature at ' + this.city + ' is ' + temp.weather.currently.temperature + '°F.' + ' The weather conditions are ' + temp.weather.currently.summary + '.' + '&hashtags=CSCI571WeatherSearch';
  }

}
