package iut.desvignes.mymeteo;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import java.io.Serializable;

/**
 * Created by androidS4 on 13/03/18.
 */

public class MeteoPresenter implements Serializable{
    private static final long serialVersionUID = 1L;

    private transient MeteoView view;
    private transient Repository repository;
    private transient LiveData<PagedList<MeteoModel>> townsList;
    private transient MeteoDao meteoDao;

    private MeteoModel lastDeletedTown;

    public void setMeteoDao(MeteoDao meteoDao){
        this.meteoDao = meteoDao;
        this.townsList = new LivePagedListBuilder<>(meteoDao.getAllTowns(), 30).build();
    }

    public void insertTestTown(){
        MeteoModel town = new MeteoModel();
        //Test avec Londres
        town.setId(2643743);
        town.setTemperature(77.77);
        town.setTownName("Londres");
        town.setIconID("09d");
        projectDao.insert(town);
    }

    public void setView(MeteoView view){
        this.view = view;
    }

    public LiveData<PagedList<MeteoModel>> getTownsList(){
        return townsList;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public void onSettings() {
        view.showMessage("onSettings");
        insertTestTown();
    }

    public void onRefresh() {
        view.showMessage("onRefresh");
    }

    public int getImageID(){
        //todo
        return R.drawable.defibrillator;
    }
}
