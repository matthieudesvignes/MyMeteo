package iut.desvignes.mymeteo;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by matth on 29/03/2018.
 */

public interface MapsView {
    void addMarker(String name, String icon, LatLng latLng);
}
