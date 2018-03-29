package iut.desvignes.mymeteo;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, MapsView, DialogMaps.Listener {

    private GoogleMap mMap;
    private Toolbar appbar;

    private MapsPresenter presenter;

    private FloatingActionButton fabMaps;
    private ExecutorService pool;

    // Classe représentant un couple Latitude/Longitude
    private LatLng currentLatLng; // = new LatLng(4.45, 750.52);
    private MeteoRoom currentTown;
    private String[] arrayIcon, arrayName;
    private double[] arrayLat, arrayLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        pool = Executors.newSingleThreadExecutor();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fabMaps = findViewById(R.id.fabMaps);

        // ToolBar
        appbar = findViewById(R.id.appbar);
        setSupportActionBar(appbar);

        if (getLastCustomNonConfigurationInstance() != null)
            presenter = (MapsPresenter) getLastCustomNonConfigurationInstance();
        else if (savedInstanceState != null)
            presenter = (MapsPresenter) savedInstanceState.getSerializable("presenter");
        else
            presenter = new MapsPresenter();
        presenter.setView(this);

        if(getIntent() != null && getIntent().getExtras() != null){
            currentTown = (MeteoRoom) getIntent().getExtras().get("currentTown");
            currentLatLng = new LatLng(currentTown.getLat(), currentTown.getLng());
            arrayLat = getIntent().getExtras().getDoubleArray("arrayLat");
            arrayLng = getIntent().getExtras().getDoubleArray("arrayLng");
            arrayIcon = getIntent().getExtras().getStringArray("arrayIcon");
            arrayName = getIntent().getExtras().getStringArray("arrayName");
       }
    }

    // ----------------- Menu Appbar --------------- //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_refresh :
                return true;
            case R.id.action_settings :
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void addMarker(String name, String icon,LatLng latLng) {
        int id = getResources().getIdentifier("icon_" + icon, "drawable", this.getPackageName());
        mMap.addMarker(new MarkerOptions().title(name)
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(id))
            );
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMapToolbarEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //addMarker(currentTown.getTownName(), currentTown.getIconID(), currentTown.getLat(), currentTown.getLng());

        presenter.drawMarkers(arrayName, arrayIcon, arrayLat, arrayLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.currentLatLng, 14));

        fabMaps.setOnClickListener(v -> {
            LatLng targetLatLng = mMap.getCameraPosition().target;
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(targetLatLng.latitude, targetLatLng.longitude, 1);
                String cityName = addresses.get(0).getAddressLine(0);
                DialogMaps.show(this, cityName);
            } catch (IOException e) {
                Log.i("Message", e.toString());
                DialogMaps.show(this, "Error : No city found");
            }
        });
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (! isChangingConfigurations())
            outState.putSerializable("presenter", presenter);
    }

    @Override
    protected void onDestroy() {
        pool.shutdown();
        super.onDestroy();
    }

    @Override
    public void onOk(DialogMaps dialog, String townName) {
        Intent intent = new Intent(this, MainActivity.class);
        Log.i("Message", "townName : " + townName);
        intent.putExtra("cityNameAdded", townName);
        startActivity(intent);
        this.finish();
    }
}

