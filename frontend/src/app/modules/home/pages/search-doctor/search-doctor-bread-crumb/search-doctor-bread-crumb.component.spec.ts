import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchDoctorBreadCrumbComponent } from './search-doctor-bread-crumb.component';

describe('SearchDoctorBreadCrumbComponent', () => {
  let component: SearchDoctorBreadCrumbComponent;
  let fixture: ComponentFixture<SearchDoctorBreadCrumbComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SearchDoctorBreadCrumbComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchDoctorBreadCrumbComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
