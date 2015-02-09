/**
 * Author: Uyen Nguyen
 * Date started: 
 * Date Completed: IP
 * Peer Review:  
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.flashcards;

import java.util.UUID;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//controls db
public class DbAdapter {
   public static final String TAG = "flashcards";
   private DbHelper dbHelper;
   private SQLiteDatabase sqlDatabase;
   private final Context context;
   
   public static final String DATABASE_NAME = "flashcards.db";  
   public static final int DATABASE_VERSION = 1;
   
   //decks table
   public static final String DECKS_TABLE_NAME = "_decks";
   public static final String DECKS_COLUMN_ID = "_id";
   public static final String DECKS_COLUMN_NAME = "_name";
   private static final String DECKS_TABLE_CREATE =
         "create table " + DECKS_TABLE_NAME
         + " ( "
         + DECKS_COLUMN_ID + " text primary key, "
         + DECKS_COLUMN_NAME + " text not null "
         + " ); ";
   private static final String DECKS_TABLE_UPGRADE =
         "Drop table if exists " + DbAdapter.DECKS_TABLE_NAME;
   
   
   //flashcards table
   public static final String FLASHCARDS_TABLE_NAME = "_flashcards";
   public static final String FLASHCARDS_COLUMN_ID = "_id";
   public static final String FLASHCARDS_COLUMN_FRONT = "_front";
   public static final String FLASHCARDS_COLUMN_BACK = "_back";
   public static final String FLASHCARDS_COLUMN_DECK = "_deck";
   private static final String FLASHCARDS_TABLE_CREATE =  
         "Create table " + FLASHCARDS_TABLE_NAME
         + " ( "
         + FLASHCARDS_COLUMN_ID + " text primary key, "
         + FLASHCARDS_COLUMN_FRONT + " text not null, "
         + FLASHCARDS_COLUMN_BACK + " text not null, "
         + FLASHCARDS_COLUMN_DECK + " text not null, "
         //foreign key from column deck  deck(id)
         + "foreign key ( " + FLASHCARDS_COLUMN_DECK + " ) references " + DECKS_TABLE_NAME + " ( " + DECKS_COLUMN_ID + " ) "
         + " );";
   private static final String FLASHCARDS_TABLE_UPGRADE =
         "Drop table if exists " + DbAdapter.FLASHCARDS_TABLE_NAME;

   
   //db helper class
   private static class DbHelper extends SQLiteOpenHelper {    
      public DbHelper(Context context, String name, CursorFactory factory, int version) {
         super(context, name, factory, version);         
         Log.d(TAG,"Created " + DATABASE_NAME);
      }

      //create tables
      @Override         
      public void onCreate(SQLiteDatabase db) {  
         db.execSQL(DbAdapter.DECKS_TABLE_CREATE);
         db.execSQL(DbAdapter.FLASHCARDS_TABLE_CREATE);
         Log.d(TAG, DECKS_TABLE_CREATE + FLASHCARDS_TABLE_CREATE);
      }
      
      //drop tables if exists
      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {         
         db.execSQL(DECKS_TABLE_UPGRADE);
         db.execSQL(FLASHCARDS_TABLE_UPGRADE);
         Log.d(TAG, DECKS_TABLE_UPGRADE + "; " + FLASHCARDS_TABLE_UPGRADE);
      }
   }
   
   //constructor
   public DbAdapter(Context context) {
      this.context = context;
   }
   
   //open db connection  
   public DbAdapter open() {
      dbHelper = new DbHelper(context, DbAdapter.DATABASE_NAME, null, DbAdapter.DATABASE_VERSION);
      sqlDatabase = dbHelper.getWritableDatabase();
      return this;
   }
   
   //close db connection
   public void close() {
      dbHelper.close();
   }
   
   //get all Decks in db
   public Cursor getAllDecks() {
      return sqlDatabase.query(DECKS_TABLE_NAME,
            new String[] {DECKS_COLUMN_ID, DECKS_COLUMN_NAME}, null, null, null, null, null);
   }
   
   //get Deck by id
   public Cursor getDeckById(String deckId) {
      return sqlDatabase.query(DECKS_TABLE_NAME,
            new String[] {DECKS_COLUMN_ID, DECKS_COLUMN_NAME},
            DECKS_COLUMN_ID + " = '" + deckId + "'", null, null, null, null);
   }
   
   //insert new Deck into Decks table
   public long insertDeck(String deckId, String deckTitle) {
      ContentValues values = new ContentValues();
      values.put(DECKS_COLUMN_ID, deckId);
      values.put(DECKS_COLUMN_NAME, deckTitle);
      return sqlDatabase.insert(DECKS_TABLE_NAME, null, values);
   }
   
   //get random Deck id
   public String getNewDeckId() {
      Log.d(TAG, "getNewDeckId ");
      String deckId = null;
      Cursor cursor = null;
      do {
         deckId = UUID.randomUUID().toString();
         cursor = getDeckById(deckId);
      } while (cursor.getCount() > 0);
      return deckId;
   }
   
   //insert new Flashcard into Flashcard table
   public void insertFlashcard(Flashcard flashcard) {
      ContentValues values = new ContentValues();
      values.put(FLASHCARDS_COLUMN_ID, flashcard.getId());
      values.put(FLASHCARDS_COLUMN_FRONT, flashcard.getFront());
      values.put(FLASHCARDS_COLUMN_BACK, flashcard.getBack());
      values.put(FLASHCARDS_COLUMN_DECK, flashcard.getDeck().getId());
      sqlDatabase.insert(FLASHCARDS_TABLE_NAME, null, values);       
      Log.d(TAG, "insertFlashcard " + flashcard.getFront());
   }
   
   //gets all Flashcards in db
   public Cursor getAllFlashcards() {
      Log.d(TAG, "getAllFlashcards");
      return sqlDatabase.query(FLASHCARDS_TABLE_NAME,
            new String[] {FLASHCARDS_COLUMN_ID, FLASHCARDS_COLUMN_FRONT, FLASHCARDS_COLUMN_BACK,  FLASHCARDS_COLUMN_DECK},
            null, null, null, null, null);
   }
   
   //get Flashcard by id
   public Cursor getFlashcardById(String flashcardId) {
      Log.d(TAG, "getFlashcardById " + flashcardId);
      return sqlDatabase.query(FLASHCARDS_TABLE_NAME,
            new String[] {FLASHCARDS_COLUMN_ID, FLASHCARDS_COLUMN_FRONT, 
            FLASHCARDS_COLUMN_BACK, FLASHCARDS_COLUMN_DECK},
            FLASHCARDS_COLUMN_ID + " = '" + flashcardId + "'", null, null, null, null);
   }
   
   //edit Flashcard
   public void editExistingFlashcard(Flashcard flashcard) {
      ContentValues newValues;
      newValues = new ContentValues();
      newValues.put(FLASHCARDS_COLUMN_FRONT, flashcard.getFront());
      newValues.put(FLASHCARDS_COLUMN_BACK, flashcard.getBack());      
      newValues.put(FLASHCARDS_COLUMN_DECK, flashcard.getDeck().getId());      
      sqlDatabase.update(FLASHCARDS_TABLE_NAME, newValues, FLASHCARDS_COLUMN_ID + " = '" + flashcard.getId() + "'", null);
      Log.d(TAG, "editExistingFlashcard " + flashcard.getFront());
   }
   
   //delete Flashcard
   public void deleteFlashcard(Flashcard flashcard) {
      deleteFlashcard(flashcard.getId());
        Log.d(TAG, "deleteFlashcard " + flashcard.getFront());
   }
   
   //delete Flashcard
   public void deleteFlashcard(String flashcardId) {
      sqlDatabase.delete(FLASHCARDS_TABLE_NAME, FLASHCARDS_COLUMN_ID + " = '" + flashcardId + "'", null);        
      Log.d(TAG, "deleteFlashcard " + flashcardId);
   }

   //get random Flashcard id
   public String getNewFlashcardId() {
      Log.d(TAG, "getNewFlashcardId ");
      String cardId = null;
      Cursor cursor = null;
      do {
         cardId = UUID.randomUUID().toString();
         cursor = getFlashcardById(cardId);
      } while (cursor.getCount() > 0);
      return cardId;
   }

}

