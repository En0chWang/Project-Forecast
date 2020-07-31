import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { FormControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css']
})
export class FormComponent implements OnInit {

  public address_info: any = {
    street: "",
    city: "",
    state: ""
  };
  public input_disabled: string = 'null';
  public city_options: string[];
  public street_options: string[];
  public states: any[] = [
    {
      name: "Alabama",
      abbreviation: "AL"
    },
    {
      name: "Alaska",
      abbreviation: "AK"
    },
    {
      name: "American Samoa",
      abbreviation: "AS"
    },
    {
      name: "Arizona",
      abbreviation: "AZ"
    },
    {
      name: "Arkansas",
      abbreviation: "AR"
    },
    {
      name: "California",
      abbreviation: "CA"
    },
    {
      name: "Colorado",
      abbreviation: "CO"
    },
    {
      name: "Connecticut",
      abbreviation: "CT"
    },
    {
      name: "Delaware",
      abbreviation: "DE"
    },
    {
      name: "District Of Columbia",
      abbreviation: "DC"
    },
    {
      name: "Federated States Of Micronesia",
      abbreviation: "FM"
    },
    {
      name: "Florida",
      abbreviation: "FL"
    },
    {
      name: "Georgia",
      abbreviation: "GA"
    },
    {
      name: "Guam",
      abbreviation: "GU"
    },
    {
      name: "Hawaii",
      abbreviation: "HI"
    },
    {
      name: "Idaho",
      abbreviation: "ID"
    },
    {
      name: "Illinois",
      abbreviation: "IL"
    },
    {
      name: "Indiana",
      abbreviation: "IN"
    },
    {
      name: "Iowa",
      abbreviation: "IA"
    },
    {
      name: "Kansas",
      abbreviation: "KS"
    },
    {
      name: "Kentucky",
      abbreviation: "KY"
    },
    {
      name: "Louisiana",
      abbreviation: "LA"
    },
    {
      name: "Maine",
      abbreviation: "ME"
    },
    {
      name: "Marshall Islands",
      abbreviation: "MH"
    },
    {
      name: "Maryland",
      abbreviation: "MD"
    },
    {
      name: "Massachusetts",
      abbreviation: "MA"
    },
    {
      name: "Michigan",
      abbreviation: "MI"
    },
    {
      name: "Minnesota",
      abbreviation: "MN"
    },
    {
      name: "Mississippi",
      abbreviation: "MS"
    },
    {
      name: "Missouri",
      abbreviation: "MO"
    },
    {
      name: "Montana",
      abbreviation: "MT"
    },
    {
      name: "Nebraska",
      abbreviation: "NE"
    },
    {
      name: "Nevada",
      abbreviation: "NV"
    },
    {
      name: "New Hampshire",
      abbreviation: "NH"
    },
    {
      name: "New Jersey",
      abbreviation: "NJ"
    },
    {
      name: "New Mexico",
      abbreviation: "NM"
    },
    {
      name: "New York",
      abbreviation: "NY"
    },
    {
      name: "North Carolina",
      abbreviation: "NC"
    },
    {
      name: "North Dakota",
      abbreviation: "ND"
    },
    {
      name: "Northern Mariana Islands",
      abbreviation: "MP"
    },
    {
      name: "Ohio",
      abbreviation: "OH"
    },
    {
      name: "Oklahoma",
      abbreviation: "OK"
    },
    {
      name: "Oregon",
      abbreviation: "OR"
    },
    {
      name: "Palau",
      abbreviation: "PW"
    },
    {
      name: "Pennsylvania",
      abbreviation: "PA"
    },
    {
      name: "Puerto Rico",
      abbreviation: "PR"
    },
    {
      name: "Rhode Island",
      abbreviation: "RI"
    },
    {
      name: "South Carolina",
      abbreviation: "SC"
    },
    {
      name: "South Dakota",
      abbreviation: "SD"
    },
    {
      name: "Tennessee",
      abbreviation: "TN"
    },
    {
      name: "Texas",
      abbreviation: "TX"
    },
    {
      name: "Utah",
      abbreviation: "UT"
    },
    {
      name: "Vermont",
      abbreviation: "VT"
    },
    {
      name: "Virgin Islands",
      abbreviation: "VI"
    },
    {
      name: "Virginia",
      abbreviation: "VA"
    },
    {
      name: "Washington",
      abbreviation: "WA"
    },
    {
      name: "West Virginia",
      abbreviation: "WV"
    },
    {
      name: "Wisconsin",
      abbreviation: "WI"
    },
    {
      name: "Wyoming",
      abbreviation: "WY"
    }
  ];
  public msg: any;
  public flag: boolean = false;
  public result_flag: boolean = true;
  public favorite_flag: boolean = false;
  public progress_bar_flag: boolean = false;
  public myControl = new FormControl();
  public myForm: FormGroup;
  public lat: string;
  public lon: string;
  public region: string;
  public city: string;
  public favorite_message: string;
  public search_status: boolean = true;
  public checkbox: boolean = false;
  public error_flag: boolean = false;
  public error: any;
  constructor(public http: HttpClient, private fb: FormBuilder) {

    this.myForm = this.fb.group({
      city: [{ value: '', disabled: false }],
      street: [{ value: '', disabled: false }],
      state: [{ value: '', disabled: false }]
    });
    this.myForm.setValidators(Validators.required);
  }


  ngOnInit() {
  }

  receiveMessage($event) {
    this.switchToResults()
    this.flag = false;
    this.progress_bar_flag = true;
    let value = $event.value;
    let city = $event.city;
    let api = 'http://homeworkeightsucks.us-west-1.elasticbeanstalk.com/search-by-current-location';
    const params = new HttpParams().set('latitude', value.lat).set('longitude', value.lon).set('region', value.state).set('city', city);
    this.http.get(api, { params }).subscribe(res => {
      console.log(res);
      this.msg = JSON.stringify(res);
      this.flag = true;
      this.progress_bar_flag = false;
    })
  }

  getCityOptions() {
    let api = 'http://homeworkeightsucks.us-west-1.elasticbeanstalk.com/city-autocomplete';
    const params = new HttpParams().set('city', this.address_info.city);
    this.http.get(api, { params }).subscribe(res => {
      var arr = res['predictions'];
      if (arr.length > 0) {
        this.city_options = new Array(arr.length);
        for (var i = 0; i < arr.length; i++) {
          this.city_options[i] = arr[i].structured_formatting.main_text;
        }
      }
    })
  }

  switchToResults() {
    this.result_flag = true;
    this.favorite_flag = false;
    document.getElementById('result_id').className = 'btn btn-info myButton';
    document.getElementById('favorite_id').className = 'btn btn-light myButton';
  }

  switchToFavorites() {
    this.favorite_flag = true;
    this.result_flag = false;
    document.getElementById('favorite_id').className = 'btn btn-info myButton';
    document.getElementById('result_id').className = 'btn btn-light myButton';
  }


  getData() {
    this.switchToResults();
    this.flag = false;
    this.progress_bar_flag = true;
    if (this.error_flag == true) {
      this.error_flag = false;
    }
    // address is entered
    if (!this.myForm.get('city').disabled) {
      let api = 'http://homeworkeightsucks.us-west-1.elasticbeanstalk.com/search-by-address';
      const params = new HttpParams().set('street', this.address_info.street).set('city', this.address_info.city).set('state', this.address_info.state);
      this.http.get(api, { params }).subscribe(res => {
        console.log(res);
        if (res['error']) {
          this.error_flag = true;
        } else {
          this.msg = JSON.stringify(res);
          this.flag = true;
        }
        this.progress_bar_flag = false;

      })
      // current location checkbox is selected
    } else {
      let api = 'http://homeworkeightsucks.us-west-1.elasticbeanstalk.com/search-by-current-location';
      const params = new HttpParams().set('latitude', this.lat).set('longitude', this.lon).set('region', this.region).set('city', this.city);
      this.http.get(api, { params }).subscribe(res => {
        console.log(res);
        this.msg = JSON.stringify(res);
        this.flag = true;
        this.progress_bar_flag = false;
      })
    }

  }

  getCurrentLocation() {
    this.checkbox = !this.checkbox;
    console.log(this.checkbox);
    let city = this.myForm.get('city');
    let state = this.myForm.get('state');
    let street = this.myForm.get('street');


    if (city.touched) { city.reset() }
    if (street.touched) { street.reset() }
    city.setValue('');
    street.setValue('');

    city.disabled ? city.enable() : city.disable();
    state.disabled ? state.enable() : state.disable();
    street.disabled ? street.enable() : street.disable();
    this.address_info.street = "";
    this.address_info.city = "";
    this.address_info.state = "";

    this.search_status = !this.search_status;
    this.http.get("http://ip-api.com/json").subscribe(res => {
      this.lat = res['lat'];
      this.lon = res['lon'];
      this.region = res['region'];
      this.city = res['city'];
      console.log(res);
    });

  }

  clearAll() {

    this.search_status = true;
    this.checkbox = false;
    // enable all input fields
    let city = this.myForm.get('city');
    let state = this.myForm.get('state');
    let street = this.myForm.get('street');
    if (city.touched) { city.reset() }
    if (street.touched) { street.reset() }
    city.setValue('');
    street.setValue('');
    if (city.disabled) { city.enable() }
    if (state.disabled) { state.enable() }
    if (street.disabled) { street.enable() }

    // clear input content
    this.address_info.street = "";
    this.address_info.city = "";
    this.address_info.state = "";

    // disable search button


    // switch back to results
    this.switchToResults();

    // disable results
    this.flag = false;
    this.error_flag = false;
    this.progress_bar_flag = false;

  }

}
