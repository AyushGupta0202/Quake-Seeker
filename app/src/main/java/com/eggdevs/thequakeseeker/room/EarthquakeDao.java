package com.eggdevs.thequakeseeker.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.eggdevs.thequakeseeker.data.CityDetails;

import java.util.List;

@Dao
public interface EarthquakeDao {

   @Insert
   void insert(CityDetails city);

   @Query("SELECT * FROM earthquake_table")
   List<Quake> getAllQuakes();
}
