import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CorporateAccountComponent } from './corporateaccount.component';

describe('CorporateAccountComponent', () => {
  let component: CorporateAccountComponent;
  let fixture: ComponentFixture<CorporateAccountComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CorporateAccountComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CorporateAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
