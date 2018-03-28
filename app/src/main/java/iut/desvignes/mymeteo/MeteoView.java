package iut.desvignes.mymeteo;

import java.util.ArrayList;

public interface MeteoView {
    void showMessage(int messageId);

    void notifyItemDeleted();

    int getIconId(String icon);

    void launchMap(MeteoRoom town);
}
