import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
@Component({
  selector: 'app-currentweather',
  templateUrl: './currentweather.component.html',
  styleUrls: ['./currentweather.component.css']
})
export class CurrentweatherComponent implements OnInit, OnChanges {

  @Input('msg') message: any;
  @Input('city') city: any;
  public msg: any;
  public flag: boolean = false;
  constructor() { }

  ngOnChanges() {
    this.msg = JSON.parse(this.message);
    this.flag = true;
    console.log("i am in onchanges");
    console.log(this.msg);
  }
  ngOnInit() {

  }



}
