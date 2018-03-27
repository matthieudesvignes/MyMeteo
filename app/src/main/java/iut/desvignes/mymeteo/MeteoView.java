package iut.desvignes.mymeteo;

/**
 * Created by androidS4 on 13/03/18.
 */

public interface MeteoView {
    void showMessage(int messageId);

    void notifyItemDeleted();

    int getIconId(String icon);
}
