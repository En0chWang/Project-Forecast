import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HourlyozoneComponent } from './hourlyozone.component';

describe('HourlyozoneComponent', () => {
  let component: HourlyozoneComponent;
  let fixture: ComponentFixture<HourlyozoneComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HourlyozoneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HourlyozoneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
