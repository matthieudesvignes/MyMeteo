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

    MeteoRoom lastTownDeleted;

    private int townCount = 5; // Pour le test du dialogue, à supprimer
    // Lien entre les classes

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


    public void onSettings() { view.showMessage("onSettings"); }

    public void onFlottingButton() {
        view.showMessage("onFloattingButton");
    }

    public void onRefresh() { view.showMessage("onRefresh"); }

    // Méthode de gestion de la BD

    public void undo() {
        meteoDao.insert(lastTownDeleted);
    }

    public void delete(MeteoRoom town) {
        lastTownDeleted = town;
        meteoDao.deleteSelectedTown(town);
        view.notifyItemDeleted();
    }

    public void refreshData() {
        //TODO
    }

    public int getImageID(){
        //todo
        return R.drawable.icon_01d;
    }


    /////////////////////////////////////////////////////
    //////////////// Méthode tests //////////////////////
    /////////////////////////////////////////////////////

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

    public void addByDialog(String name){
        MeteoRoom town = new MeteoRoom();
        town.setId(townCount);
        town.setTemperature(3.14);
        town.setTownName(name);
        town.setIconID("09d");
        meteoDao.insert(town);
        townCount ++ ;
    }

       public void insertTestTown(){
            //Test 1
            MeteoRoom town = new MeteoRoom();
            town.setId(1);
            town.setTemperature(45);
            town.setTownName("berlin");
            town.setIconID("09d");

            //Test 2
            MeteoRoom town2 = new MeteoRoom();
            town2.setId(2);
            town2.setTemperature(42.42);
            town2.setTownName("Moscou");
            town2.setIconID("09d");

           //Test 3
           MeteoRoom town3 = new MeteoRoom();
           town3 .setId(3);
           town3.setTemperature(0);
           //town3.setTownName("xaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"); Un nom trop long ne passe pas
           town3.setTownName("new york");
           town3.setIconID("09d");

           //Test 3
           MeteoRoom town4 = new MeteoRoom();
           town4 .setId(4);
           town4.setTemperature(0);
           town4.setTownName("Paris");
           town4.setIconID("09d");

            meteoDao.insert(town, town2, town3, town4);
        }

    public void accessApiTest(){
           MeteoModel res = repository.getTownByName("paris");
           view.showMessage("nom : " + res.getName() + " iconId : " +res.getIcon());
    }
}
