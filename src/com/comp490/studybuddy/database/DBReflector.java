package com.comp490.studybuddy.database;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.comp490.studybuddy.models.Attributes;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBReflector {
	   SQLiteDatabase db;
	   
	   public DBReflector(SQLiteDatabase db) {
		   this.db = db;
	   }
	   
	   public String createTableFromClass(String className) {
		   Field[] fields = null;
		   StringBuilder query = new StringBuilder();
		   
		   //Get the fields out of the class
		   try {
			   Class<?> obj = Class.forName(className);
			   fields = obj.getDeclaredFields();
			   String name = obj.getSimpleName();
			   query.append("CREATE TABLE " + name + " (");
		   } catch (Exception e) {
			   Log.e("ERROR", e.getStackTrace().toString());
		   }
		   
		   boolean first = true;
		   for(Field field : fields) {
			   if(!first) {
				   query.append(", ");
			   }
			   query.append(field.getName()+" ");
			   
			   if(String.class.isAssignableFrom(field.getType())) {
				   query.append("TEXT");
			   }
			   if(field.getType() == Integer.TYPE) {
				   query.append("INTEGER");
			   }
			   
			   Annotation note = field.getAnnotation(Attributes.class);
			   if(note != null) {
				   if(note instanceof Attributes) {
					   Attributes attr = (Attributes) note;
					   if(attr.primaryKey()) {
						   query.append(" PRIMARY KEY");
					   }
				   }
			   }
			   first = false;
		   }
		   
		   query.append(");");
		   return query.toString();
	   }
	   
	   public ArrayList<Object> createClassFromTable(String className) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		   Field[] fields = null;
		   String name = null;
		   Class<?> obj = null;
		   StringBuilder query = new StringBuilder();
		   
		   //Get the fields out of the class
		   try {
			   obj = Class.forName(className);
			   fields = obj.getDeclaredFields();
			   name = obj.getSimpleName();
		   } catch (Exception e) {
			   Log.e("ERROR", e.getStackTrace().toString());
		   }
		   
		   ArrayList<Object> list = new ArrayList<Object>();
		   Cursor cursor = db.query(name, null, null, null, null, null, null);
		   if(cursor.moveToFirst()) {
			   while(!cursor.isAfterLast()) {
				   Object cons = obj.getConstructor().newInstance();
				   for(Field field : fields) {
					   if(String.class.isAssignableFrom(field.getType())) {
						   field.set(cons, cursor.getString(cursor.getColumnIndex(field.getName())));
					   }
					   if(field.getType() == Integer.TYPE) {
						   field.set(cons, cursor.getInt(cursor.getColumnIndex(field.getName())));
					   }
					   
					   if(String.class.isAssignableFrom(field.getType())) {
						   query.append("TEXT");
					   }
					   if(field.getType() == Integer.TYPE) {
						   query.append("INTEGER");
					   }
					   list.add(cons);
					   cursor.moveToNext();
				   }
			   }
		   }
		   return list;
	   }

}
