import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HourlywindspeedComponent } from './hourlywindspeed.component';

describe('HourlywindspeedComponent', () => {
  let component: HourlywindspeedComponent;
  let fixture: ComponentFixture<HourlywindspeedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HourlywindspeedComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HourlywindspeedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
