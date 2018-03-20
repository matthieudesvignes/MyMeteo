package iut.desvignes.mymeteo;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PagedList;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;

import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
/**
 * Created by androidS4 on 06/03/18.
 */
@Dao
public interface MeteoDao {

    @Query("SELECT * FROM town_table")
    DataSource.Factory<Integer, MeteoModel> getAllTowns();

    @Query("SELECT * FROM town_table")
    List<MeteoModel> getAllTownsList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MeteoModel... towns);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<MeteoModel> towns);

    @Query("SELECT * FROM town_table LIMIT 1")
    MeteoModel getFirstTown();

    @Delete
    void deleteAll(List<MeteoModel> towns);

    @Delete
    void deleteSelectedTown(MeteoModel town);

    @Update
    void update(MeteoModel... towns);
}