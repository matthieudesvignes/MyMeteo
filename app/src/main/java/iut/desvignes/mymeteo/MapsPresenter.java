package iut.desvignes.mymeteo;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by matth on 29/03/2018.
 */

public class MapsPresenter implements Serializable {
        private static final long serialVersionUID = 1L;

        private transient MapsView view;

    public void setView(MapsView view){
        this.view = view;
    }

    public void drawMarkers(String[] arrayName, String[] arrayIcon, double[] arrayLat, double[] arrayLng){
        for(int i = 0; i < arrayIcon.length; i++){
            view.addMarker(arrayName[i], arrayIcon[i], new LatLng(arrayLat[i], arrayLng[i]));
        }
    }
}
