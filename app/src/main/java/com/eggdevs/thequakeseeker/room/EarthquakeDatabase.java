package com.eggdevs.thequakeseeker.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = Quake.class, version = 1, exportSchema = false)
public abstract class EarthquakeDatabase extends RoomDatabase {

   public abstract EarthquakeDao getEarthquakeDao();
   private static volatile EarthquakeDatabase INSTANCE;

   private static final int NUMBER_OF_THREADS = 4;
   static final ExecutorService databaseWriteExecutor =
           Executors.newFixedThreadPool(NUMBER_OF_THREADS);

   static EarthquakeDatabase getDatabase(final Context context) {
      if (INSTANCE == null) {
         synchronized (EarthquakeDatabase.class) {
            if (INSTANCE == null) {
               INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                       EarthquakeDatabase.class, "earthquake_database")
                       .build();
            }
         }
      }
      return INSTANCE;
   }
}
