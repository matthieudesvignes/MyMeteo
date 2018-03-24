package iut.desvignes.mymeteo;

import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
            pool.submit(() ->
                    presenter.accessApiTest());
        });

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
                presenter.onRefresh();
                pool.submit(() -> presenter.refreshData());
                //startActivity(new Intent(this, MapsActivity.class)); TODO
                return true;
            case R.id.action_settings :
                presenter.onSettings();
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


    // --------- Méthode Adapter
    @Override
    public void notifyItemInserted(int index) {

    }

    @Override
    public void notifyItemDeleted(int index) {

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

    // gestion threads
    private boolean isUiThread(){
        return Looper.myLooper() == Looper.getMainLooper();
    }


}
