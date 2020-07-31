import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WeathertapComponent } from './weathertap.component';

describe('WeathertapComponent', () => {
  let component: WeathertapComponent;
  let fixture: ComponentFixture<WeathertapComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WeathertapComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WeathertapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
