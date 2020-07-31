import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HourlytemperatureComponent } from './hourlytemperature.component';

describe('HourlytemperatureComponent', () => {
  let component: HourlytemperatureComponent;
  let fixture: ComponentFixture<HourlytemperatureComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HourlytemperatureComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HourlytemperatureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
