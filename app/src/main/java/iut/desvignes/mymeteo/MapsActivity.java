package iut.desvignes.mymeteo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Toolbar appbar;

    private Marker selectedMarker = null;

    private LocationManager locationManager;
    private LocationListener locationListener;

    // Classe représentant un couple Latitude/Longitude
    private LatLng currentLatLng = new LatLng(49.240065, 4.062048); // batiment U

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // ToolBar
        appbar = findViewById(R.id.appbar);
        setSupportActionBar(appbar);

        // Location manager et Listener

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 5));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };
    }

    // ----------------- Menu Appbar --------------- //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, R.string.action_settings, Toast.LENGTH_SHORT).show();
                return true;

            /*case R.id.action_addItem:
                Toast.makeText(this, R.string.action_addItem, Toast.LENGTH_SHORT).show();
                addMarker("titre default", -1);
                break;
            case R.id.action_addItemDefibrilator:
                addMarker("titre defibrilator", R.drawable.defibrillator);
                break;
            case R.id.action_addItemExit:
                addMarker("titre door", R.drawable.door);
                break;
            case R.id.action_deleteMarker:
                if(selectedMarker != null)
                    selectedMarker.remove();*/

        }

        return super.onOptionsItemSelected(item);
    }

    private void addMarker(String title, int iconId) {

        mMap.clear();

        if(iconId != -1){
            mMap.addMarker(new MarkerOptions().title(title)
                    .position(currentLatLng)
                    .icon(BitmapDescriptorFactory.fromResource(iconId))
            );}
        else{
            mMap.addMarker(new MarkerOptions().title(title)
                    .position(currentLatLng)
                    .icon(BitmapDescriptorFactory.defaultMarker())
            );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO :Check de permission à faire
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
            @Override
            public boolean onMyLocationButtonClick()
            {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 19));
                return false;
            }
        });

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 1));


        // ---------- OnClick Listener : Map & Marker -------- //
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){

            @Override
            public void onMapClick(LatLng latLng) {
                selectedMarker = null;
                Toast.makeText(MapsActivity.this, latLng.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                selectedMarker = marker;
                Toast.makeText(MapsActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        /* Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney)); */
    }

    // ---------- Gestion position listener ----------- //
    @Override
    protected void onStart() {
        super.onStart();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO :Check de permission à faire
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }

    @Override
    protected void onStop() {
        locationManager.removeUpdates(locationListener);
        super.onStop();

    }
}

