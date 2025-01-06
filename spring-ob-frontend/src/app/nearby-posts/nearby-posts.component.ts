import { Component, OnInit } from '@angular/core';
import * as L from 'leaflet';
import { UserService } from '../service';
import { HttpClient } from '@angular/common/http';
import { PostService } from '../service/post.service';
import { PostReadDto, RabbitCareObject } from '../model/postRead.model';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-nearby-posts',
  templateUrl: './nearby-posts.component.html',
  styleUrls: ['./nearby-posts.component.css']
})
export class NearbyPostsComponent implements OnInit {

  private map: any;
  private loggedInUser = this.userService.currentUser;
  posts: PostReadDto[] = [];
  rabbitCareObjects: RabbitCareObject[] = [];

  constructor(private userService : UserService,
    private http: HttpClient,
    private postService: PostService
  ) {

   }

  ngOnInit(): void {

    this.postService.getAllRabbitObjects().subscribe({
      next: (response) => {
        this.rabbitCareObjects = response;
        console.log('Rabbit objects:', response);
      },
      error: (err) => {
        console.error('Error fetching rabbit objects:', err);
      },
    });
   
   this.postService.getAllPostsDescByDate().subscribe({
    next: (response) => {
      this.posts = response;
      console.log('Posts:', response);
    },
    error: (err) => {
      console.error('Error fetching posts:', err);
    },
  });

    this.getCoordinates(this.loggedInUser!.address).subscribe((data: any) => {
      console.log(this.loggedInUser!.address);
      console.log(data);
      if (data && data.length > 0) {
        const lat = data[0].lat;
        const lon = data[0].lon;
        
    setTimeout(() => this.initMap(lat, lon));
    
    setTimeout(function () {
      window.dispatchEvent(new Event("resize"));
   }, 500);
        
      }
    });
   
  }

  private initMap(lat : number, lon : number): void {

    


    this.map = L.map('map', {
      center: [lat, lon],
      zoom: 17,              
    });

    const tiles = L.tileLayer(
        'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
        {
            maxZoom: 20,
            minZoom: 3,
            attribution: '<span style="font-weight: bold; color: rgba(15, 147, 141)">Mapa objava, klinika i azila za zeke&nbsp;&nbsp;&nbsp;&nbsp;</span>',
        }
    );

    tiles.addTo(this.map);

    this.displayPostsOnMap();

    this.displayRabbitCareObjectsOnMap();

}

private getCoordinates(address: string) {
  const apiUrl = `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(address)}`;
  return this.http.get(apiUrl);
}

private displayPostsOnMap() {
  

    this.posts.forEach(post => {
    const postIcon = L.icon({
      iconUrl: '../assets/images/post.png',
      iconAnchor: [20, 40], // Adjust the icon anchor to align with the marker
    });
    console.log(post.location?.latitude + " " + post.creator.username + " " + post.description);

    const marker = L.marker([post.location!.longitude, post.location!.latitude], { 
      icon: postIcon, 
      draggable: false 
    }).addTo(this.map);

    marker.bindPopup(`
      <div style="
        display: flex; 
        flex-direction: column; 
        width: 250px; 
        border-radius: 8px; 
        overflow: hidden; 
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); 
        font-family: Arial, sans-serif;">
        
        <!-- Image Section -->
        <div style="
          height: 150px; 
          background-image: url('data:image/png;base64,${post.imageBase64}');
          background-size: cover; 
          background-position: center;">
        </div>
        
        <!-- Content Section -->
        <div style="padding: 10px; background-color: white;">
          <div style="
            display: flex; 
            align-items: center; 
            margin-bottom: 10px;">
            <div style="
              width: 40px; 
              height: 40px; 
              border-radius: 50%; 
              background-color: lightgray; 
              margin-right: 10px; 
              background-image: url('path-to-profile-image'); 
              background-size: cover; 
              background-position: center;">
            </div>
            <b>${post.creator.username}</b>
          </div>
          
          <div style="
            font-size: 14px; 
            color: #333; 
            margin-bottom: 10px;">${post.description}</div>
          
          <div style="
            display: flex; 
            justify-content: space-between; 
            font-size: 12px; 
            color: #888;">
            <span><b>${post.likes}</b> Likes</span>
            <span><b>${post.numOfComments}</b> Comments</span>
          </div>
        </div>
      </div>
    `, { offset: L.point(0, -20) });
    


  });
}


private displayRabbitCareObjectsOnMap() {
  this.rabbitCareObjects.forEach(object => {
    const postIcon = L.icon({
      iconUrl: '../assets/images/shelter.png',
      iconAnchor: [20, 40],
    });

    const marker = L.marker([object.longitude, object.latitude], { 
      icon: postIcon, 
      draggable: false 
    }).addTo(this.map);

    marker.bindPopup(
      `<b>${object.name}</b>`,
      {
        offset: L.point(0, -20),
      }
    );
  });

}
}
