import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HourlypressureComponent } from './hourlypressure.component';

describe('HourlypressureComponent', () => {
  let component: HourlypressureComponent;
  let fixture: ComponentFixture<HourlypressureComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HourlypressureComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HourlypressureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
