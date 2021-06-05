package com.eggdevs.thequakeseeker.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "earthquake_table")
public class Quake {

   @ColumnInfo(name = "text")
   @NonNull
   private final String text;

   @PrimaryKey(autoGenerate = true)
   private int id;

   public Quake(String text) {
      this.text = text;
   }

   public String getText() {
      return text;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }}
