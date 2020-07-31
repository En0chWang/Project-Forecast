import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ChartsModule } from 'ng2-charts';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MatAutocompleteModule, MatInputModule, MatTooltipModule } from '@angular/material';
import { StorageServiceModule } from 'angular-webstorage-service';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormComponent } from './components/form/form.component';
import { CurrentweatherComponent } from './components/currentweather/currentweather.component';
import { HourlyweatherComponent } from './components/hourlyweather/hourlyweather.component';
import { HourlytemperatureComponent } from './components/hourlytemperature/hourlytemperature.component';
import { HourlypressureComponent } from './components/hourlypressure/hourlypressure.component';
import { HourlyhumidityComponent } from './components/hourlyhumidity/hourlyhumidity.component';
import { HourlyozoneComponent } from './components/hourlyozone/hourlyozone.component';
import { HourlyvisibilityComponent } from './components/hourlyvisibility/hourlyvisibility.component';
import { HourlywindspeedComponent } from './components/hourlywindspeed/hourlywindspeed.component';
import { WeathertapComponent } from './components/weathertap/weathertap.component';
import { NgbdModalContent, WeeklyweatherComponent } from './components/weeklyweather/weeklyweather.component';
import { FavoritesComponent } from './components/favorites/favorites.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';
import { TransferHttpCacheModule } from '@nguniversal/common';
import { NgtUniversalModule } from '@ng-toolkit/universal';
@NgModule({
  declarations: [
    AppComponent,
    FormComponent,
    CurrentweatherComponent,
    HourlyweatherComponent,
    HourlytemperatureComponent,
    HourlypressureComponent,
    HourlyhumidityComponent,
    HourlyozoneComponent,
    HourlyvisibilityComponent,
    HourlywindspeedComponent,
    WeathertapComponent,
    WeeklyweatherComponent,
    NgbdModalContent,
    FavoritesComponent
  ],
  imports: [
    BrowserModule.withServerTransition({ appId: 'serverApp' }),
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ChartsModule,
    NgbModule,
    BrowserModule,
    StorageServiceModule,
    BrowserAnimationsModule,
    MatAutocompleteModule,
    MatInputModule,
    ReactiveFormsModule,
    MatTooltipModule,
    CommonModule,
    TransferHttpCacheModule,
    NgtUniversalModule
  ],
  providers: [],
  bootstrap: [AppComponent],
  entryComponents: [
    NgbdModalContent
  ]
})
export class AppModule { }
