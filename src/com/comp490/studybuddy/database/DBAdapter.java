
/**
 * Author: Uyen Nguyen
 * Date started: 
 * Date Completed: IP
 * Peer Review:  
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.database;

import java.util.UUID;

import com.comp490.studybuddy.flashcards.Flashcard;
import com.comp490.studybuddy.models.CalendarEventModel;
import com.comp490.studybuddy.todolist.Task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//controls db
public class DBAdapter {

   public static final String TAG = "database";
   
   private DbHelper dbHelper;
   private SQLiteDatabase sqlDatabase;
   
   private final Context context;
   
   public static final String DATABASE_NAME = "studybuddy.db";
   
   public static final int DATABASE_VERSION = 5;
   
   public static final String TASK_TABLE_NAME = "tasks";
   public static final String TASK_COLUMN_ID = "_id";
   public static final String TASK_COLUMN_NAME = "_title";
   public static final String TASK_COLUMN_DATE = "_date";
   public static final String TASK_COLUMN_TIME = "_time";
   public static final String TASK_COLUMN_PRIORITY = "_priority";
   public static final String TASK_COLUMN_NOTIFICATION = "_notification";
   
   private static final String CREATE_TASK_TABLE =
		   "Create table " + TASK_TABLE_NAME
   + " ( "
   + TASK_COLUMN_ID + " integer primary key autoincrement, "
   + TASK_COLUMN_NAME + " text not null, "
   + TASK_COLUMN_DATE + " integer not null, "
   + TASK_COLUMN_TIME + " integer not null, "
   + TASK_COLUMN_PRIORITY + " integer not null, "
   + TASK_COLUMN_NOTIFICATION + " integer not null);";
   
   private static final String TASK_TABLE_UPGRADE = 
		   "Drop table if exists " + DBAdapter.TASK_TABLE_NAME;

	private static final String CAL_EVENT_TABLE = "calEvents";
	private static final String CAL_EVENT_ID = "_eventId";
	private static final String CAL_EVENT_NAME = "_eventName";
	private static final String CAL_EVENT_START_DATE = "_startDate";
	private static final String CAL_EVENT_END_DATE = "_endDate";
	private static final String CAL_EVENT_DESCRIPTION = "_description";
	private static final String CAL_EVENT_COLOR = "_color";
	
	private static final String CREATE_CAL_EVENT_TABLE = 
			"create table " + CAL_EVENT_TABLE + " ( " +
					CAL_EVENT_ID + " text primary key, " +
					CAL_EVENT_NAME + " text not null, " + 
					CAL_EVENT_START_DATE + " text not null, " +
					CAL_EVENT_END_DATE + " text," +
					CAL_EVENT_DESCRIPTION + " text, " + 
					CAL_EVENT_COLOR + " text " + " ); ";
	
	private static final String CAL_EVENT_TABLE_UPGRADE =
	         "Drop table if exists " + CAL_EVENT_TABLE;

	
	public static final String NOTE_TABLE_NAME 					= "notes";
	public static final String NOTE_COLUMN_ID 					= "_id";
	public static final String NOTE_COLUMN_NAME					= "_name";
	public static final String NOTE_COLUMN_TYPE 				= "_type";
	public static final String NOTE_COLUMN_PATH 				= "_path";
	public static final String NOTE_COLUMN_VIEWID 				= "_viewid";
	public static final String NOTE_COLUMN_SECONDARY_VIEWID 	= "_viewid2";
	
	private static final String CREATE_NOTE_TABLE =
			String.format("create table %s" +
					"(%s integer primary key, %s text not null, %s text not null, %s text," +
					"%s integer, %s integer);",
					NOTE_TABLE_NAME, 
					NOTE_COLUMN_ID, NOTE_COLUMN_NAME, NOTE_COLUMN_TYPE, NOTE_COLUMN_PATH,
					NOTE_COLUMN_VIEWID, NOTE_COLUMN_SECONDARY_VIEWID);
	
	private static final String NOTE_TABLE_UPGRADE =
			String.format("Drop table if exists %s", NOTE_TABLE_NAME);
	
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
	         "Drop table if exists " + DECKS_TABLE_NAME;
	   
	   
	   //flashcards table
	   public static final String FLASHCARDS_TABLE_NAME = "_flashcards";
	   public static final String FLASHCARDS_COLUMN_ID = "_id";
	   public static final String FLASHCARDS_COLUMN_FRONT = "_front";
	   public static final String FLASHCARDS_COLUMN_BACK = "_back";
	   public static final String FLASHCARDS_COLUMN_DECK = "_deck";
	   public static final String FLASHCARDS_TABLE_CREATE =  
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
	         "Drop table if exists " + FLASHCARDS_TABLE_NAME;
	
   //db helper class
	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			Log.d(TAG, "Created " + DATABASE_NAME);
		}

		// create Task table
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, CREATE_TASK_TABLE);
			db.execSQL(CREATE_TASK_TABLE);
			
			Log.d(TAG, CREATE_CAL_EVENT_TABLE);
			db.execSQL(CREATE_CAL_EVENT_TABLE);
			
			Log.d(TAG, CREATE_NOTE_TABLE);
			db.execSQL(CREATE_NOTE_TABLE);

			db.execSQL(DECKS_TABLE_CREATE);
			db.execSQL(FLASHCARDS_TABLE_CREATE);
			Log.d(TAG, DECKS_TABLE_CREATE + FLASHCARDS_TABLE_CREATE);
		}

		// drop Task table if exists
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(TASK_TABLE_UPGRADE);
			Log.d(TAG, TASK_TABLE_UPGRADE);
			db.execSQL(CAL_EVENT_TABLE_UPGRADE);
			Log.d(TAG, CAL_EVENT_TABLE_UPGRADE);
			Log.d(TAG, NOTE_TABLE_UPGRADE);
			db.execSQL(NOTE_TABLE_UPGRADE);
	        db.execSQL(DECKS_TABLE_UPGRADE);
	        db.execSQL(FLASHCARDS_TABLE_UPGRADE);
	        Log.d(TAG, DECKS_TABLE_UPGRADE + "; " + FLASHCARDS_TABLE_UPGRADE);
			this.onCreate(db);
		}
	}
   
   //constructor, pass the current activity to the context
   public DBAdapter(Context context) {
      this.context = context;
   }
   
   //open db connection
   public DBAdapter open() {
      dbHelper = new DbHelper(context, DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
      sqlDatabase = dbHelper.getWritableDatabase();
      return this;
   }
   
   //close db connection
   public void close() {
      dbHelper.close();
   }

   //insert new task into Task table
   public void insertTask(Task task) {
      ContentValues initialValues = new ContentValues();
      initialValues.put(TASK_COLUMN_NAME, task.getName());
      initialValues.put(TASK_COLUMN_DATE, task.getDate().getTimeInMillis());
      initialValues.put(TASK_COLUMN_TIME, task.getTime().getTimeInMillis());
      initialValues.put(TASK_COLUMN_PRIORITY, task.getPriority());
      initialValues.put(TASK_COLUMN_NOTIFICATION, task.getNotification());
      sqlDatabase.insert(TASK_TABLE_NAME, null, initialValues);       
      Log.d(TAG, "insertTask " + task.getName());
   }
   
   //get all tasks in db
   public Cursor getAllTasks() {
      Log.d(TAG, "getAllTasks");
      return sqlDatabase.query(TASK_TABLE_NAME,
            new String[] {TASK_COLUMN_ID, TASK_COLUMN_NAME, 
            TASK_COLUMN_DATE, TASK_COLUMN_TIME, 
            TASK_COLUMN_PRIORITY, TASK_COLUMN_NOTIFICATION},
            null, null, null, null, null);
   }
   
   //get task by id
   public Cursor getTaskById(String taskId) {
      Log.d(TAG, "getTaskById " + taskId);
      return sqlDatabase.query(TASK_TABLE_NAME,
            new String[] {TASK_COLUMN_ID, TASK_COLUMN_NAME, 
            TASK_COLUMN_DATE, TASK_COLUMN_TIME, 
            TASK_COLUMN_PRIORITY, TASK_COLUMN_NOTIFICATION},
            TASK_COLUMN_ID + " = '" + taskId + "'", null, null, null, null);
   }
   
   //edit existing task
   public void editExistingTask(Task task) {
      ContentValues updateValues;
      // Update Task table first
      updateValues = new ContentValues();
      updateValues.put(TASK_COLUMN_NAME, task.getName());
      updateValues.put(TASK_COLUMN_DATE, task.getDate().getTimeInMillis());      
      updateValues.put(TASK_COLUMN_TIME, task.getTime().getTimeInMillis());
      updateValues.put(TASK_COLUMN_PRIORITY, task.getPriority());
      updateValues.put(TASK_COLUMN_NOTIFICATION, task.getNotification());
      sqlDatabase.update(TASK_TABLE_NAME, updateValues, TASK_COLUMN_ID + " = '" + task.getId() + "'", null);
      Log.d(TAG, "editExistingTask " + task.getName());
   }
   
   //delete Task
   public void deleteTask(Task task) {
      deleteTask(task.getId());
        Log.d(TAG, "deleteTask " + task.getName());
   }
   
   //delete Task
   public void deleteTask(int taskId) {
      sqlDatabase.delete(TASK_TABLE_NAME, TASK_COLUMN_ID + " = '" + taskId + "'", null);        
      Log.d(TAG, "deleteTask " + taskId);
   }
   
	public Cursor getAllEvents() {
		return sqlDatabase.query(CAL_EVENT_TABLE, 
				new String[] {CAL_EVENT_ID, CAL_EVENT_NAME, CAL_EVENT_START_DATE, 
					CAL_EVENT_END_DATE, CAL_EVENT_DESCRIPTION, CAL_EVENT_COLOR}, 
				null, null, null, null, null);
	}
	
	public void insertEvent(CalendarEventModel event) {
		ContentValues values = new ContentValues();
		values.put(CAL_EVENT_ID, event.getId());
		values.put(CAL_EVENT_NAME, event.getName());
		values.put(CAL_EVENT_START_DATE, event.getStart());
		values.put(CAL_EVENT_END_DATE, event.getEnd());
		values.put(CAL_EVENT_DESCRIPTION, event.getDesc());
		values.put(CAL_EVENT_COLOR, event.getColor());
	    sqlDatabase.insert(CAL_EVENT_TABLE, null, values); 
	}
	
	public Cursor getEventById(String eventId) {
	   Log.d(TAG, "getTaskById " + eventId);
	   return sqlDatabase.query(CAL_EVENT_TABLE,
	         new String[] {CAL_EVENT_ID, CAL_EVENT_NAME, CAL_EVENT_START_DATE, 
			   CAL_EVENT_END_DATE, CAL_EVENT_DESCRIPTION, CAL_EVENT_COLOR},
	         CAL_EVENT_ID + " = '" + eventId + "'", null, null, null, null);
	}
	
	// For Day Details grabs events for a current day 
	public Cursor getEventByDay(String curr) {
		Log.d(TAG, "getEventsByDay: " +curr);
		return sqlDatabase.query(CAL_EVENT_TABLE,
    		new String[] {CAL_EVENT_ID, CAL_EVENT_NAME, CAL_EVENT_START_DATE, 
				CAL_EVENT_END_DATE, CAL_EVENT_DESCRIPTION, CAL_EVENT_COLOR},
	   		CAL_EVENT_START_DATE + " LIKE " + "'" + curr + "%'",
	   		null, null, null, null);
	}
	
	// For the main Calendar View grabs events for the current month
	// As well as the previous month and next month
	public Cursor getEventByYear(String prevMonth, String curr, String nextMonth) {
		Log.d(TAG, "getEventsByTime:" +prevMonth+ " " +curr + " " +nextMonth);
		return sqlDatabase.query(CAL_EVENT_TABLE,
    		new String[] {CAL_EVENT_ID, CAL_EVENT_NAME, CAL_EVENT_START_DATE, 
				CAL_EVENT_END_DATE, CAL_EVENT_DESCRIPTION, CAL_EVENT_COLOR},
    		CAL_EVENT_START_DATE + " LIKE " + "'" + prevMonth + "%'" + " OR " +
	   		CAL_EVENT_START_DATE + " LIKE " + "'" + curr + "%'" + " OR " +
	   		CAL_EVENT_START_DATE + " LIKE " + "'" + nextMonth + "%'", null, null, null, null);
	}
	
	public void editExistingTask(CalendarEventModel event) {
		ContentValues values = new ContentValues();
		values.put(CAL_EVENT_ID, event.getId());
		values.put(CAL_EVENT_NAME, event.getName());
		values.put(CAL_EVENT_START_DATE, event.getStart());
		values.put(CAL_EVENT_END_DATE, event.getEnd());
		values.put(CAL_EVENT_DESCRIPTION, event.getDesc());
		values.put(CAL_EVENT_COLOR, event.getColor());
		sqlDatabase.update(CAL_EVENT_TABLE, values, CAL_EVENT_ID + " = '" + 
				event.getId() + "'", null);
		Log.d(TAG, "editExistingTask " + event.getName());
	}
	
	//delete Task
	public void deleteEvent(CalendarEventModel event) {
		String eventId = event.getId();
		sqlDatabase.delete(CAL_EVENT_TABLE, CAL_EVENT_ID + " = '" + eventId + "'", null); 
		Log.d(TAG, "deleteEvent " + event.getName());
	}
	
	public String getNewEventId() {
	   Log.d(TAG, "getEventById ");
	   String eventId = null;
	   Cursor cursor = null;
	   do {
	      eventId = UUID.randomUUID().toString();
	      cursor = getEventById(eventId);
	   } while (cursor.getCount() > 0);
	   return eventId;
	}

	public Cursor getAllNotes() {
		Log.d(TAG, "getAllNotes");
		return sqlDatabase.query(NOTE_TABLE_NAME, new String[] {
				NOTE_COLUMN_ID, NOTE_COLUMN_NAME, NOTE_COLUMN_TYPE,
				NOTE_COLUMN_PATH, NOTE_COLUMN_VIEWID,
				NOTE_COLUMN_SECONDARY_VIEWID },
				null, null, null, null, null, null);
	}
	
	public boolean insertNote(int id, String name, String type, String path, int vId, int vId2) {
		ContentValues initval = new ContentValues();
		initval.put(NOTE_COLUMN_ID, id);
		initval.put(NOTE_COLUMN_NAME, name);
		initval.put(NOTE_COLUMN_TYPE, type);
		initval.put(NOTE_COLUMN_PATH, path);
		initval.put(NOTE_COLUMN_VIEWID, vId);
		initval.put(NOTE_COLUMN_SECONDARY_VIEWID, vId2);
		Log.d(TAG, String.format("insertNote %s", name));
		return (sqlDatabase.insert(NOTE_TABLE_NAME, null, initval) > -1);

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

}
