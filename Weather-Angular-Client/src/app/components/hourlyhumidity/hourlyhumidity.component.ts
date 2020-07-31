import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-hourlyhumidity',
  templateUrl: './hourlyhumidity.component.html',
  styleUrls: ['./hourlyhumidity.component.css']
})
export class HourlyhumidityComponent implements OnInit, OnChanges {

  @Input('msg') msg: string;

  getHumidityData(message: string) {
    let msg = JSON.parse(message);
    let temp: string[] = [];
    for (var index = 0; index < 24; index++) {
      temp[index] = msg.weather.hourly.data[index].humidity;
    }
    return temp;
  }

  public barChartHumidityOptions: any;

  public barChartLabels: any;
  public barChartType: any;
  public barChartLegend: any;

  // Humidity
  public barChartHumidityData: any[];


  constructor() { }

  ngOnChanges(changes: SimpleChanges) {
    // basic bar chart properties
    this.barChartLabels = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23'];
    this.barChartType = 'bar';
    this.barChartLegend = true;

    // Humidity
    this.barChartHumidityOptions = {
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
            labelString: 'Humidity (%)'
          }
        }]
      }
    };
    this.barChartHumidityData = [
      { data: this.getHumidityData(this.msg), label: 'Humidity', backgroundColor: '#a5d0ee', hoverBackgroundColor: '#43809b' }
    ];

  }

  ngOnInit() {
  }

}
