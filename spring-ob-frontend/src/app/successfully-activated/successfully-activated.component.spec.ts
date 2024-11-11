import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SuccessfullyActivatedComponent } from './successfully-activated.component';

describe('SuccessfullyActivatedComponent', () => {
  let component: SuccessfullyActivatedComponent;
  let fixture: ComponentFixture<SuccessfullyActivatedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SuccessfullyActivatedComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SuccessfullyActivatedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
