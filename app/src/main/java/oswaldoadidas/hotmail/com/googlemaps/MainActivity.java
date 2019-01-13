package oswaldoadidas.hotmail.com.googlemaps;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap mgoogleMap;
    GoogleApiClient mgoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (googleServicesAvailable()) {
            Toast.makeText(this, "Se logró", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_main);
            iniciarMapa();
        } else {

        }

    }

    private void iniciarMapa() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);
    }

    public boolean googleServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int estaDisponible = apiAvailability.isGooglePlayServicesAvailable(this);
        if (estaDisponible == ConnectionResult.SUCCESS) {
            return true;

        } else if (apiAvailability.isUserResolvableError(estaDisponible)) {
            Dialog dialog = apiAvailability.getErrorDialog(this, estaDisponible, 0);
            dialog.show();

        } else {
            Toast.makeText(this, "No se puede conectar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgoogleMap = googleMap;

        if(mgoogleMap !=null){

            mgoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    MainActivity.this.setMarker("Local ",latLng.latitude,latLng.longitude);


                }
            });


            mgoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    Geocoder geocoder = new Geocoder(MainActivity.this);
                    LatLng latLng = marker.getPosition();
                    double lat = latLng.latitude;
                    double lg = latLng.longitude;
                    List<Address> list = null;
                    try {
                        list = geocoder.getFromLocation(lat,lg,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = list.get(0);
                    marker.setTitle(address.getLocality());
                    marker.showInfoWindow();

                }
            });

            mgoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.info_window,null);
                    TextView tvLocality = (TextView)v.findViewById(R.id.tv_locality);
                    TextView tvLat = (TextView)v.findViewById(R.id.tv_lat);
                    TextView tvLng = (TextView)v.findViewById(R.id.tv_lng);
                    TextView tvSnippet = (TextView)v.findViewById(R.id.tv_snippet);

                    LatLng latLng = marker.getPosition();
                    tvLocality.setText(marker.getTitle());
                    tvLat.setText("Latitud: "+latLng.latitude);
                    tvLng.setText("Longitud: "+latLng.longitude);
                    tvSnippet.setText(marker.getSnippet());
                    return v;
                }
            });

        }
        irLocalizadorZoom(19.390726,-99.1220992,15);


        /*
       if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
       }
        mgoogleMap.setMyLocationEnabled(true);
        */


        /*
        mgoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mgoogleApiClient.connect();
    */


    }

    private void irLocalizador(double latitud, double longitd) {
        LatLng latLng = new LatLng(latitud, longitd);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
        mgoogleMap.moveCamera(cameraUpdate);


    }

    private void irLocalizadorZoom(double latitud, double longitd, float zoom) {
        LatLng latLng = new LatLng(latitud, longitd);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mgoogleMap.moveCamera(cameraUpdate);
    }

    Marker marker;



    public void Aller(View view) throws IOException {
        EditText editText = (EditText) findViewById(R.id.editText);
        String localizacion = editText.getText().toString();

        Geocoder geocoder = new Geocoder(this);
        List<Address> lista = geocoder.getFromLocationName(localizacion, 1);
        Address address = lista.get(0);
        String localidad = address.getLocality();

        Toast.makeText(this, localidad, Toast.LENGTH_SHORT).show();

        double latitud = address.getLatitude();
        double longitud = address.getLongitude();
        irLocalizadorZoom(latitud, longitud, 15);

        setMarker(localidad, latitud, longitud);

    }

    //Circle circle;


    /*Marker marker1;
    Marker marker2;
    Polyline line;*/

    ArrayList<Marker> markers = new ArrayList<Marker>();
    static  final int POLYGON_POINTS = 5;
    Polygon shape;


    private void setMarker(String localidad, double latitud, double longitud) {
        /*
        if(marker!=null){
            removeEverything();
        }*/

        if(markers.size() == POLYGON_POINTS){
            removeEverything();


        }


        MarkerOptions options = new MarkerOptions()
                .title(localidad)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_sipply))
                .position(new LatLng(latitud,longitud))
                .snippet("Salut tout le monde");

        markers.add(mgoogleMap.addMarker(options));

        if(markers.size() == POLYGON_POINTS){
            drawPolygon();

        }

        /*if(marker1 == null){
            marker1 = mgoogleMap.addMarker(options);
        }else if(marker2 == null){
            marker2 = mgoogleMap.addMarker(options);
            drawLine();

        }else{
            removeEverything();
            marker1 = mgoogleMap.addMarker(options);
        }*/


        //circle = drawCircle(new LatLng(latitud,longitud));
    }

    private void drawPolygon() {

        PolygonOptions options = new PolygonOptions()
                .fillColor(0x33FF0000)
                .strokeWidth(3)
                .strokeColor(Color.BLUE);

        for(int i=0; i<POLYGON_POINTS; i++){
            options.add(markers.get(i).getPosition());

        }
        shape = mgoogleMap.addPolygon(options);


    }

    private void removeEverything() {
        for(Marker marker: markers){
            marker.remove();

        }
        markers.clear();
        shape.remove();
        shape = null;

    }

    /*private void drawLine() {
        PolylineOptions options = new PolylineOptions()
                .add(marker1.getPosition())
                .add(marker2.getPosition())
                .color(Color.BLUE)
                .width(3);
        line = mgoogleMap.addPolyline(options);
    }*/

    /*
    private Circle drawCircle(LatLng latLng){

        CircleOptions options = new CircleOptions()
                .center(latLng)
                .radius(1000)
                .fillColor(0x33FF0000)
                .strokeColor(Color.CYAN)
                .strokeWidth(3);
        return mgoogleMap.addCircle(options);
    }
    */

    /*private void removeEverything(){
        marker1.remove();
        marker1 = null;
        marker2.remove();
        marker2 = null;
        line.remove();
        //circle.remove();
        //circle = null;

    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mapTypeNone:
                mgoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                mgoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mgoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mgoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mgoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);

    }

    LocationRequest locationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        }
        //LocationServices.FusedLocationApi.requestLocationUpdates(mgoogleApiClient, locationRequest, this);
          LocationServices.FusedLocationApi.requestLocationUpdates(mgoogleApiClient,locationRequest,this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null){
            Toast.makeText(this,"No se puede dar localidad actual",Toast.LENGTH_LONG).show();

        }else{
            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,15);
            mgoogleMap.animateCamera(update);
        }

    }
}
