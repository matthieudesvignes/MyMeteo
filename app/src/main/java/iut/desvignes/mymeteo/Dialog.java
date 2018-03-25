package iut.desvignes.mymeteo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by matth on 25/03/2018.
 */

public class Dialog extends DialogFragment {

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        // création d'une "usine" de boîtes de dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        EditText name = new EditText(getActivity());
        builder.setView(name);

        // configuration de l'usine
        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.dialog_message);
        builder.setPositiveButton(R.string.ok,
                (dialog, which) -> {
                    Listener listener = (Listener) getActivity();
                    listener.onOk(Dialog.this, name.getText().toString());
                });
        builder.setNegativeButton(R.string.cancel, null);

        // création de la boîte de dialogue
        return builder.create();
    }

    public static void show(AppCompatActivity activity) {
        Dialog dialog = new Dialog();
        dialog.show(activity.getSupportFragmentManager(), "Dialog");
    }

    public static interface Listener {
        void onOk(Dialog dialog, String townName);
    }
}

