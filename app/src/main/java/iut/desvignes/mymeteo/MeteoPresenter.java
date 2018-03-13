package iut.desvignes.mymeteo;

import java.util.ArrayList;

/**
 * Created by androidS4 on 13/03/18.
 */

public class MeteoPresenter {
    private MeteoView view;
    ArrayList<MeteoModel> models;


    public MeteoPresenter(){
        models = new ArrayList<>();
        models.add(new MeteoModel("New York"));
        models.add(new MeteoModel("Tokio"));
        models.add(new MeteoModel("Paris"));
    }

    public void setView(MeteoView view){
        this.view = view;
    }

    public void onSettings() {
        view.showMessage("onSettings");
    }

    public void onRefresh() {
        view.showMessage("onRefresh");
    }
}
