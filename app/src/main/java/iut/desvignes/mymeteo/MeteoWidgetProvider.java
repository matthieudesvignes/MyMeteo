package iut.desvignes.mymeteo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by matth on 29/03/2018.
 */

public class MeteoWidgetProvider extends AppWidgetProvider {

    private static MeteoRoom favoriteTown;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        setDefaultTown();
        // Perform this loop procedure for each App Widget that belongs to this
        // provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            updateAppWidget(context, appWidgetManager, appWidgetId, favoriteTown);
        }
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        setDefaultTown();
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        setDefaultTown();

    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, MeteoRoom town) {

        // Prepare widget views
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setTextViewText(R.id.nameWidget, town.getTownName());
        views.setTextViewText(R.id.tempWidget, Double.toString(town.getTemperature()) +"°C");

        int id = context.getResources().getIdentifier("icon_" + town.getIconID(), "drawable", context.getPackageName());
        views.setImageViewResource(R.id.imageWidget, id);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static void setFavoriteTown(MeteoRoom town){
        favoriteTown = town;
    }

    static void setDefaultTown(){
        favoriteTown = new MeteoRoom();
        favoriteTown.setTownName("Pas de ville favori");
        favoriteTown.setTemperature(42);
        favoriteTown.setIconID("01d");
    }

}
