package com.comp490.studybuddy.database;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.comp490.studybuddy.models.Attributes;

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

}
