package iut.desvignes.mymeteo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MeteoView{

    //Initialisation des vues de l'activité
    private Toolbar appBar;

    MeteoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Affectation des vues de l'activité
        appBar = findViewById(R.id.appbar);
        setSupportActionBar(appBar);

        //Lien Vue-Presenter
        presenter = new MeteoPresenter();
        presenter.setView(this);
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
                return true;
            case R.id.action_settings :
                presenter.onSettings();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //Méthodes de l'interface Vue
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
}
