package iut.desvignes.mymeteo;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.widget.Toast;

import java.io.Serializable;

/**
 * Created by androidS4 on 13/03/18.
 */

public class MeteoPresenter implements Serializable{
    private static final long serialVersionUID = 1L;

    private transient MeteoView view;
    private transient Repository repository;
    private transient LiveData<PagedList<MeteoRoom>> townsList;
    private transient MeteoDao meteoDao;


    public void setMeteoDao(MeteoDao meteoDao){
        this.meteoDao = meteoDao;
        this.townsList = new LivePagedListBuilder<>(meteoDao.getAllTowns(), 30).build();
    }

    public void setView(MeteoView view){
        this.view = view;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public LiveData<PagedList<MeteoRoom>> getTownsList(){
        return townsList;
    }

    // ------------- Méthode bouton du menu --------------- //


    public void onSettings() {
        view.showMessage("onSettings");
        //insertTestTown();
    }

    public void onFlottingButton() {
        view.showMessage("onFloattingButton");
    }

    public void onRefresh() {
        view.showMessage("onRefresh");
       // MeteoModel town = repository.getTownByName("London");
        //meteoDao.insert(town);
    }

    public void refreshData() {
    }

    // Méthode invoqué par le dialogue
    public void addTown(String townName){
        MeteoModel townRetrofit = repository.getTownByName(townName);
        MeteoRoom town= new MeteoRoom();
        town.setIconID(townRetrofit.getIcon());
        town.setId(townRetrofit.getId());
        town.setLat(townRetrofit.getLat());
        town.setLng(townRetrofit.getLon());
        town.setTemperature(townRetrofit.getTemp());
        town.setTownName(townRetrofit.getName());
        meteoDao.insert(town);
    }

    public int getImageID(){
        //todo
        return R.drawable.icon_01d;
    }

       public void insertTestTown(){
            //meteoDao.deleteAll(meteoDao.getAllTownsList());
            MeteoRoom town = new MeteoRoom();
            //Test avec Londres
            town.setId(3);
            town.setTemperature(45);
            town.setTownName("berlin");
            town.setIconID("09d");
            MeteoRoom town2 = new MeteoRoom();
            //Test avec Londres
            town2.setId(2);
            town2.setTemperature(42.42);
            town2.setTownName("Moscou");
            town2.setIconID("09d");
            meteoDao.insert(town, town2);
        }

    public void accessApiTest(){
           MeteoModel res = repository.getTownByName("paris");
           view.showMessage("nom : " + res.getName() + " iconId : " +res.getIcon());
    }
}
