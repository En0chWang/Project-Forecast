import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HourlyvisibilityComponent } from './hourlyvisibility.component';

describe('HourlyvisibilityComponent', () => {
  let component: HourlyvisibilityComponent;
  let fixture: ComponentFixture<HourlyvisibilityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HourlyvisibilityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HourlyvisibilityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
