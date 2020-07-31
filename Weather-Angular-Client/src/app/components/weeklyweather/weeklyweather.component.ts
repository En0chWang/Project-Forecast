import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import * as CanvasJS from 'canvasjs-2.3.2/canvasjs.min';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'ngbd-modal-content',
  template: `
  <div class="modal-header" style="background-color: #43809b">
  <h4 class="modal-title">{{timeLabel}}</h4>
  <button type="button" class="close" aria-label="Close" (click)="activeModal.dismiss('Cross click')">
      <span aria-hidden="true">&times;</span>
  </button>
</div>
<div class="modal-body" style="background-color: #7acaef;padding-left:20px;padding-right:20px">
  <table>
    <tr>
      <td style="width:50%">
        <div style="font-size:24px">
          {{city}}
        </div>
        <div style="font-size:36px">
          {{msg.temperature}} &#8457;
        </div>
        <div>
         {{msg.summary}}
        </div>
      </td>
      <td style="width:50%">
        <img style="max-width:80%;max-height: 80%;" src={{picLink}} />
      </td>
    </tr>
  </table>
  <div class="container" style="border-top: solid #6ebee2">
      <div class="row" style="padding-top:20px">
          <div class="col-sm-6"></div>
          <div class="col-sm-6">
              <div class="row-sm-2">
                  <div class="col">
                      Precipitation: {{msg.precipIntensity}}
                  </div>
              </div>
              <div class="row-sm-2">
                  <div class="col">
                      Chance of Rain: {{msg.precipProbability * 100}} %
                  </div>
              </div>
              <div class="row-sm-2">
                  <div class="col">
                      Wind Speed: {{msg.windSpeed}} mph
                  </div>
              </div>
              <div class="row-sm-2">
                  <div class="col">
                      Humidity: {{msg.humidity * 100}} %
                  </div>
              </div>
              <div class="row-sm-2">
                  <div class="col">
                      Visibility: {{msg.visibility}} miles
                  </div>
              </div>
              <div class="row-sm-2">
                  <div class="col"></div>
              </div>
          </div>
      </div>
  </div>
</div>
  `
})
export class NgbdModalContent {
  @Input() msg: any;
  @Input() timeLabel: any;
  @Input() picLink: any;
  @Input() city: any;
  constructor(public activeModal: NgbActiveModal) { }
}

@Component({
  selector: 'app-weeklyweather',
  templateUrl: './weeklyweather.component.html',
  styleUrls: ['./weeklyweather.component.css']
})

export class WeeklyweatherComponent implements OnInit, OnChanges {

  @Input('msg') msg: any;
  @Input('city') irrelavent: any;
  constructor(public http: HttpClient, private modalService: NgbModal) { }


  public currentIndex: any;
  public modalData: any;
  public flag: boolean = false;
  public timeLabel: string;
  public picLink: string;
  public city: string;

  openModal(data: any) {
    const modalRef = this.modalService.open(NgbdModalContent);
    modalRef.componentInstance.msg = data;
    modalRef.componentInstance.timeLabel = this.timeLabel;
    modalRef.componentInstance.picLink = this.picLink;
    modalRef.componentInstance.city = this.city;
  }
  getData(e: any) {
    var lat = e.dataPoint.dataToSend.lat;
    var lon = e.dataPoint.dataToSend.lon;
    var time = e.dataPoint.dataToSend.time;
    this.timeLabel = e.dataPoint.label;
    let apiURL = 'http://homeworkeightsucks.us-west-1.elasticbeanstalk.com/search-by-weekly-data';
    const params = new HttpParams().set('latitude', lat).set('longitude', lon).set('time', time);
    this.http
      .get(apiURL, { params })
      .subscribe(res => {
        this.modalData = res['msg'].currently;
        this.modalData.temperature = parseFloat(this.modalData.temperature).toFixed(0);
        this.modalData.precipIntensity = parseFloat(this.modalData.precipIntensity).toFixed(2);
        this.picLink = this.assignImageLink(this.modalData.icon);

        this.openModal(this.modalData);
        this.flag = true;
      });
  }
  assignImageLink(icon: string) {
    let link: string;
    if (icon == "clear-day" || icon == "clear-night") {
      link = 'https://cdn3.iconfinder.com/data/icons/weather-344/142/sun-512.png';
    } else if (icon == "rain") {
      link = 'https://cdn3.iconfinder.com/data/icons/weather-344/142/rain-512.png';
    } else if (icon == "snow") {
      link = 'https://cdn3.iconfinder.com/data/icons/weather-344/142/snow-512.png';
    } else if (icon == "sleet") {
      link = 'https://cdn3.iconfinder.com/data/icons/weather-344/142/lightning-512.png';
    } else if (icon == "wind") {
      link = 'https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_10-512.png';
    } else if (icon == "fog") {
      link = 'https://cdn3.iconfinder.com/data/icons/weather-344/142/cloudy-512.png';
    } else if (icon == "cloudy") {
      link = 'https://cdn3.iconfinder.com/data/icons/weather-344/142/cloud-512.png';
    } else if (icon == "partly-cloudy-day" || icon == "partly-cloudy-night") {
      link = 'https://cdn3.iconfinder.com/data/icons/weather-344/142/sunny-512.png';
    }
    return link;
  }


  getTemperature(message: any) {

    let msg = JSON.parse(message);

    let temp: { x: any, y: any[], label: any, dataToSend: { lat: any, lon: any, time: any } }[] = new Array(8);
    for (var index = 0; index < 8; index++) {
      let lat = msg.weather.latitude;
      let lon = msg.weather.longitude;
      let time = msg.weather.daily.data[index].time;
      let unix = new Date(msg.weather.daily.data[index].time * 1000);
      var date = unix.getDate();
      var month = unix.getMonth() + 1;
      var year = unix.getFullYear();

      temp[index] = {
        x: 10 * (index + 1),
        y: [msg.weather.daily.data[index].temperatureLow, msg.weather.daily.data[index].temperatureHigh],
        label: date + '/' + month + '/' + year,
        dataToSend: {
          lat: lat,
          lon: lon,
          time: time
        }
      }
    }
    return temp;
  }

  ngOnChanges() {
    this.city = JSON.parse(this.msg).city;
    var chart = new CanvasJS.Chart("chartContainer", {
      animationEnabled: true,
      exportEnabled: false,
      title: {
        text: "Weekly Weather"
      },
      axisX: {
        title: 'Days',
        gridThickness: 0
      },
      axisY: {
        includeZero: false,
        title: "Temperature in Fahrenheit",
        interval: 10,
        gridThickness: 0
      },
      legend: {
        verticalAlign: "top"
      },
      data: [{
        type: "rangeBar",
        showInLegend: true,
        legendText: "Day wise temperature range",
        color: '#a5d0ee',
        yValueFormatString: "#0#",
        indexLabel: "{y[#index]}",
        toolTipContent: "<b>{label}</b>: {y[0]} to {y[1]}",
        click: e => { this.getData(e) },
        dataPoints: this.getTemperature(this.msg)
      }]
    });

    chart.render();
  }

  ngOnInit() {

  }
}


