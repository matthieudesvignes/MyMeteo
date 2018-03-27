package iut.desvignes.mymeteo;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;

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


    // Méthode de gestion de la BD

    public void undo() {
        meteoDao.insert(lastTownDeleted);
    }

    public void delete(MeteoRoom town) {
        lastTownDeleted = town;
        meteoDao.deleteSelectedTown(town);
        view.notifyItemDeleted();
    }

    public void refreshData(boolean isNetworkOn, boolean pref, ExecutorService service) {
        if(!isNetworkOn)
            view.showMessage(R.string.network_off);
        else if(!pref)
            view.showMessage(R.string.enable_refresh);
        else{
            view.showMessage(R.string.action_refresh);
            //meteoDao.refreshAll() TODO
        }

    }

    public int getImageID(MeteoRoom town){
        return view.getIconId(town.getIconID());
    }

    // Méthode invoqué par le dialogue
    public void addTown(String name){
        MeteoModel townRetrofit = repository.getTownByName(name);
        MeteoRoom town = MeteoModel.createMeteoRoom(townRetrofit);
        meteoDao.insert(town);
    }
}
