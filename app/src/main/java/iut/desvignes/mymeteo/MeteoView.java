package iut.desvignes.mymeteo;

/**
 * Created by androidS4 on 13/03/18.
 */

public interface MeteoView {
    void showMessage(String message);

    void notifyItemInserted(int index);

    void notifyItemDeleted(int index);
}
