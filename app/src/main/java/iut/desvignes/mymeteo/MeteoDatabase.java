package iut.desvignes.mymeteo;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by androidS4 on 19/03/18.
 */
@Database(entities = {MeteoRoom.class}, version = 2)
public abstract class MeteoDatabase extends RoomDatabase{

    public abstract MeteoDao getMeteoDao();

    private static MeteoDatabase instance;

    public static synchronized MeteoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MeteoDatabase.class,
                    "meteo.db")
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return instance;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE meteo_table ADD COLUMN lat DOUBLE") ;
            db.execSQL("ALTER TABLE meteo_table ADD COLUMN lng DOUBLE") ;
        }
    };
}
