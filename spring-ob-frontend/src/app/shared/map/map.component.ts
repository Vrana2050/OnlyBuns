import { Component, OnInit, Output } from '@angular/core';
import Map from 'ol/Map';
import Tile from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import View from 'ol/View';
import * as ol from 'ol';
import { fromLonLat } from 'ol/proj';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import VectorSource from 'ol/source/Vector';
import VectorLayer from 'ol/layer/Vector';
import { Coordinate } from 'ol/coordinate';
import { EventEmitter } from '@angular/core';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css'],
})
export class MapComponent implements OnInit {
  @Output() coordinatesExported = new EventEmitter<{ latitude: number, longitude: number }>(); // Emitovanje koordinata
  map: ol.Map | undefined;
  lat: number | undefined;
  lon: number | undefined;
  vectorLayer: VectorLayer | undefined;

  constructor() { }

  ngOnInit(): void {
    this.initMap();
  }

  // Inicijalizacija mape
  private initMap(): void {
    const tileLayer = new Tile({
      source: new OSM(),
    });
    // Kreiraj mapu sa OpenLayers
    const vectorSource = new VectorSource();
    this.vectorLayer = new VectorLayer({
      source: vectorSource,
    });

     this.map = new ol.Map({
      target: 'map', // ID HTML elementa u kojem će biti mapa
      layers: [tileLayer, this.vectorLayer], // Dodaj tile i vector sloj
      view: new ol.View({
        center: fromLonLat([19.8227, 45.2396]), // Koordinate Novog Sada
        zoom: 13,
      }),
    });
    // Dodaj event za klik na mapu
    this.map.on('click', (event) => {
      const coords = event.coordinate;  // Koordinate gde je kliknuto (u proj4 koordinatama)
      const lonLat = fromLonLat(coords);  // Pretvori u Lon/Lat koordinatni sistem

      this.lon = lonLat[0];
      this.lat = lonLat[1];

      if(lonLat[0] && lonLat[1])
      {
      this.emitCoordinates();
      this.addMarker(coords);
      }
      else
      {
        alert('Please select a valid location');
      }
    });
  }
  private addMarker(coordinates: Coordinate): void {
    const marker = new Feature({
      geometry: new Point(coordinates),
    });

    // Dodaj marker u vector sloj
    if (this.vectorLayer) {
      this.vectorLayer.getSource()?.addFeature(marker);
    }
  }
  private emitCoordinates(): void {
    const coordinates = {
      latitude: this.lat || 0,
      longitude: this.lon || 0,
    };
    this.coordinatesExported.emit(coordinates); // Emitovanje koordinata roditeljskom komponentu
  }
}
