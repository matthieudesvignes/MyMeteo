package iut.desvignes.mymeteo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, Dialog.Listener{

    private GoogleMap mMap;
    private Toolbar appbar;

    private MeteoPresenter presenter;
    /*private FloatingActionButton fab;
    private ExecutorService pool;*/

    // Classe représentant un couple Latitude/Longitude
    private LatLng currentLatLng; // = new LatLng(4.45, 750.52);
    private MeteoRoom currentTown;
    private MeteoDao meteoDao;
    private List<MeteoRoom> townsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //pool = Executors.newSingleThreadExecutor();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*fab = findViewById(R.id.fab);
        fab.setOnClickListener(v->{
            Dialog.show(this);
        });*/

        // ToolBar
        appbar = findViewById(R.id.appbar);
        setSupportActionBar(appbar);

       if(getIntent() != null && getIntent().getExtras() != null){
            currentTown = (MeteoRoom) getIntent().getExtras().get("currentTown");
            currentLatLng = new LatLng(currentTown.getLat(), currentTown.getLng());
            //presenter = (MeteoPresenter) getIntent().getExtras().get("presenter");
        }
    }

    // ----------------- Menu Appbar --------------- //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, R.string.action_settings, Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void addMarker(String name, String icon, double lat, double lng) {
        mMap.clear();

        int id = getResources().getIdentifier("icon_" + icon, "drawable", this.getPackageName());

        mMap.addMarker(new MarkerOptions().title(name)
                    .position(new LatLng(lat, lng))
                    .icon(BitmapDescriptorFactory.fromResource(id))
            );
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.currentLatLng, 14));

        townsList = meteoDao.getAllTownsList();
        for(int i = 0; i < townsList.size(); i++){
            addMarker(townsList.get(i).getTownName(), townsList.get(i).getIconID(), townsList.get(i).getLat(), townsList.get(i).getLng());
        }
    }

    @Override
    public void onOk(Dialog dialog, String townName) {
        //pool.submit(()->presenter.addTown(townName));
    }


}

