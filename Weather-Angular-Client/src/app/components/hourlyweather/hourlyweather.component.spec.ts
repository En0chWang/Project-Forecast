import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HourlyweatherComponent } from './hourlyweather.component';

describe('HourlyweatherComponent', () => {
  let component: HourlyweatherComponent;
  let fixture: ComponentFixture<HourlyweatherComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HourlyweatherComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HourlyweatherComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
