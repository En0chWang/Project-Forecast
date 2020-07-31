import {
  Component,
  Input, ViewChild, ElementRef,
  OnInit,
  SimpleChanges, OnChanges
} from '@angular/core';

@Component({
  selector: 'app-hourlyweather',
  templateUrl: './hourlyweather.component.html',
  styleUrls: ['./hourlyweather.component.css']
})
export class HourlyweatherComponent implements OnChanges, OnInit {

  @Input('msg') msg: string;

  public divName: string = "Temperature";


  constructor() { }

  ngOnChanges(changes: SimpleChanges) {

  }

  ngOnInit() {
  }

  switchDiv(value: string) {
    this.divName = value;
  }


}
