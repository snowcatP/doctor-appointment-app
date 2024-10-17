import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchDoctorFilterComponent } from './search-doctor-filter.component';

describe('SearchDoctorFilterComponent', () => {
  let component: SearchDoctorFilterComponent;
  let fixture: ComponentFixture<SearchDoctorFilterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SearchDoctorFilterComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchDoctorFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
