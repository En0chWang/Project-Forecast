import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HourlyhumidityComponent } from './hourlyhumidity.component';

describe('HourlyhumidityComponent', () => {
  let component: HourlyhumidityComponent;
  let fixture: ComponentFixture<HourlyhumidityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HourlyhumidityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HourlyhumidityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
