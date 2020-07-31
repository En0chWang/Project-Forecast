import {
  Component, OnInit,
  Input,
  SimpleChanges, OnChanges
} from '@angular/core';

@Component({
  selector: 'app-hourlypressure',
  templateUrl: './hourlypressure.component.html',
  styleUrls: ['./hourlypressure.component.css']
})
export class HourlypressureComponent implements OnInit, OnChanges {

  @Input('msg') msg: string;

  getPressureData(message: string) {
    let msg = JSON.parse(message);
    let temp: string[] = [];
    for (var index = 0; index < 24; index++) {
      temp[index] = msg.weather.hourly.data[index].pressure;
    }
    return temp;
  }

  public barChartPressureOptions: any;

  public barChartLabels: any;
  public barChartType: any;
  public barChartLegend: any;

  // pressure
  public barChartPressureData: any[];


  constructor() { }

  ngOnChanges(changes: SimpleChanges) {
    // basic bar chart properties
    this.barChartLabels = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23'];
    this.barChartType = 'bar';
    this.barChartLegend = true;

    // Pressure
    this.barChartPressureOptions = {
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
    this.barChartPressureData = [
      { data: this.getPressureData(this.msg), label: 'Pressure', backgroundColor: '#a5d0ee', hoverBackgroundColor: '#43809b' }
    ];

  }

  ngOnInit() {
  }

}
