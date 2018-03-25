package iut.desvignes.mymeteo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements MeteoView{

    //Initialisation des vues de l'activité
    private Toolbar appBar;
    private RecyclerView recyclerView;
    private ExecutorService pool;
    private MeteoPresenter presenter;
    private MeteoAdapter adapter;

    private FloatingActionButton floatingActionButton;
    private CoordinatorLayout meteoCoordinator;

    // Check le réseau, active le bouton refresh si connecté, désactive sinon
    private boolean isNetworkOn;
    private boolean getPrefRefresh;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    ConnectivityManager cm  = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                    if (networkInfo != null)
                        isNetworkOn = true;
                    else
                        isNetworkOn = false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pool = Executors.newSingleThreadExecutor();

        //Affectation des vues de l'activité
        appBar = findViewById(R.id.appbar);
        setSupportActionBar(appBar);
        meteoCoordinator = findViewById(R.id.meteoCoordinator);
        floatingActionButton = findViewById(R.id.fab);

        // on click du bouton flottant
        floatingActionButton.setOnClickListener(view ->{
            presenter.onFlottingButton();
            pool.submit(() -> presenter.addByDialog("azertyuiop")); }); // TODO

        //Lien Vue-Presenter + gestion du cycle de vie
        if (getLastCustomNonConfigurationInstance() != null)
            presenter = (MeteoPresenter) getLastCustomNonConfigurationInstance();
        else if (savedInstanceState != null)
            presenter = (MeteoPresenter) savedInstanceState.getSerializable("presenter");
        else
            presenter = new MeteoPresenter();
        presenter.setView(this);

        //Lien avec la BD
        MeteoDatabase db = MeteoDatabase.getInstance(this);
        presenter.setMeteoDao(db.getMeteoDao());

        // gestion du recycler View
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // gestion adapter + repository
        adapter = new MeteoAdapter(pool, presenter);


        presenter.getTownsList().observe(this, towns -> {
                                                                adapter.submitList(towns);
                                                            });


        recyclerView.setAdapter(adapter);
        presenter.setRepository(new Repository());

        // Rafraichissement des données
        pool.submit(() -> presenter.refreshData());
    }

    //------ Méthode pour le menu appBar -------//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_refresh :
                if(!isNetworkOn)
                    Toast.makeText(this, R.string.network_off, Toast.LENGTH_SHORT).show();
                else if(!getPrefRefresh)
                    Toast.makeText(this, R.string.Refresh_button_disabled, Toast.LENGTH_SHORT).show();
                else{
                    presenter.onRefresh();
                    pool.submit(() -> presenter.refreshData());
                }
                return true;
            case R.id.action_settings :
                //presenter.onSettings();
                startActivity(new Intent(this, SettingsActivity.class));

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //------ Méthodes de l'interface Vue -------//
    @Override
    public void showMessage(String message)
    {
        if(!isUiThread()){
            runOnUiThread(() -> showMessage(message));
            return;
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    // --------- Méthode Adapter ----------------- //



    @Override
    public void notifyItemDeleted() {

        Snackbar.make(meteoCoordinator, R.string.item_deleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pool.submit(() -> presenter.undo());
                    }
                }).show();
    }


    //---------- Cycle de vie --------//
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
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(receiver, filter);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        getPrefRefresh = prefs.getBoolean("enable_refresh", true);
    }

    @Override
    protected void onPause() {
        this.unregisterReceiver(receiver);
        super.onPause();
    }

    // gestion threads
    private boolean isUiThread(){
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
