package iut.desvignes.mymeteo;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
        floatingActionButton.setOnClickListener(view ->{
            pool.submit(() ->
                    presenter.onRefresh());
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
                pool.submit(() -> presenter.onRefresh());
                //startActivity(new Intent(this, MapsActivity.class)); TODO
                return true;
            case R.id.action_settings :
                pool.submit(() -> presenter.onSettings());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //------ Méthodes de l'interface Vue -------//
    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

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
}
