import { Component, OnInit } from '@angular/core';
import { SlickCarouselModule } from 'ngx-slick-carousel';
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [SlickCarouselModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit {
  ngOnInit(): void {}
  slideConfig = {
    "centerMode": true,
    "centerPadding": '60px',
    "slidesToShow": 3,
    "responsive": [
      {
        "breakpoint": 768,
        "settings": {
          "arrows": false,
          "centerMode": true,
          "centerPadding": '40px',
          "slidesToShow": 3,
        },
      },
      {
        "breakpoint": 480,
        "settings": {
          "arrows": false,
          "centerMode": true,
          "centerPadding": '40px',
          "slidesToShow": 1,
        },
      },
    ],
  };
}