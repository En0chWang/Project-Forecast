import {
  Component, OnInit,
  Input,
  SimpleChanges, OnChanges
} from '@angular/core';

@Component({
  selector: 'app-hourlytemperature',
  templateUrl: './hourlytemperature.component.html',
  styleUrls: ['./hourlytemperature.component.css']
})
export class HourlytemperatureComponent implements OnInit, OnChanges {

  @Input('msg') msg: string;

  getTemperatureData(message: string) {
    let msg = JSON.parse(message);
    let temp: string[] = [];
    for (var index = 0; index < 24; index++) {
      temp[index] = msg.weather.hourly.data[index].temperature;
    }
    return temp;
  }

  public barChartTemperatureOptions: any;
  public temperatureFlag: boolean = false;

  public barChartLabels: any;
  public barChartType: any;
  public barChartLegend: any;

  // temperature
  public barChartTemperatureData: any[];


  constructor() { }

  ngOnChanges(changes: SimpleChanges) {
    // basic bar chart properties
    this.barChartLabels = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23'];
    this.barChartType = 'bar';
    this.barChartLegend = true;

    // temperature
    this.barChartTemperatureOptions = {
      scaleShowVerticalLines: false,
      responsive: true,
      scales: {
        xAxes: [{
          scaleLabel: {
            display: true,
            labelString: 'Time difference from current hour'
          }
        }],
        yAxes: [{
          scaleLabel: {
            display: true,
            labelString: 'Fahrenheit'
          }
        }]
      }
    };
    this.barChartTemperatureData = [
      { data: this.getTemperatureData(this.msg), label: 'Temperature', backgroundColor: '#a5d0ee', hoverBackgroundColor: '#43809b' }
    ];
    this.temperatureFlag = true;
  }

  ngOnInit() {
  }

}
