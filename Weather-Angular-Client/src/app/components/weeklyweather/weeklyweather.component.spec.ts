import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WeeklyweatherComponent } from './weeklyweather.component';

describe('WeeklyweatherComponent', () => {
  let component: WeeklyweatherComponent;
  let fixture: ComponentFixture<WeeklyweatherComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WeeklyweatherComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WeeklyweatherComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
