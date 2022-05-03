package hr.vsite.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Date;

public class dbHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME="CroSOSdb";
    private static final int DATABASE_VERSION=1;

    private static final String PERSON_TABLE_NAME="person";
    private static final String PERSON_COLUMN_ID="_id_person";
    private static final String PERSON_COLUMN_NAME="person_name";
    private static final String PERSON_COLUMN_SURNAME="person_surname";
    private static final String PERSON_COLUMN_GENDER="person_gender";
    private static final String PERSON_COLUMN_DATE_OF_BIRTH="person_date_of_birth";
    private static final String PERSON_COLUMN_COUNTRY="person_country";
    private static final String PERSON_COLUMN_BLOOD_TYPE="person_blood_type";
    private static final String PERSON_COLUMN_RH_FACTOR="person_rh_factor";
    private static final String PERSON_COLUMN_ALLERGIES="person_allergies";
    private static final String PERSON_COLUMN_MEDICAL_CONDITIONS="person_medical_condition";

    public dbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE " + PERSON_TABLE_NAME + "(" + PERSON_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PERSON_COLUMN_NAME + " TEXT, " + PERSON_COLUMN_SURNAME + " TEXT, " + PERSON_COLUMN_GENDER + " TEXT, " +
                PERSON_COLUMN_DATE_OF_BIRTH + " TEXT, " + PERSON_COLUMN_COUNTRY + " TEXT, " + PERSON_COLUMN_BLOOD_TYPE + " TEXT, " +
                PERSON_COLUMN_RH_FACTOR + " TEXT, " + PERSON_COLUMN_ALLERGIES + " TEXT, " + PERSON_COLUMN_MEDICAL_CONDITIONS + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME);
        onCreate(db);
    }

    public long addPerson(String name, String surname, String gender, String dateOfBirth,String country,String bloodType,String rhFactor, String allergies,String medicalConditions){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues CV= new ContentValues();

        CV.put(PERSON_COLUMN_NAME,name);
        CV.put(PERSON_COLUMN_SURNAME,surname);
        CV.put(PERSON_COLUMN_GENDER,gender);
        CV.put(PERSON_COLUMN_DATE_OF_BIRTH, dateOfBirth);
        CV.put(PERSON_COLUMN_COUNTRY,country);
        CV.put(PERSON_COLUMN_BLOOD_TYPE,bloodType);
        CV.put(PERSON_COLUMN_RH_FACTOR,rhFactor);
        CV.put(PERSON_COLUMN_ALLERGIES,allergies);
        CV.put(PERSON_COLUMN_MEDICAL_CONDITIONS,medicalConditions);

        return db.insert(PERSON_TABLE_NAME,null,CV);

    }

    public Cursor readAllPersonData(){
        String query= "SELECT * FROM " + PERSON_TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor = null;

        if (db!=null){
            cursor=db.rawQuery(query,null);
        }
        return cursor;
    }
    public Cursor readPersonDataByID(String row_id){
        String query= "SELECT * FROM " + PERSON_TABLE_NAME + " WHERE " + PERSON_COLUMN_ID + " = " + row_id;
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=null;

        if(db!=null){
            cursor=db.rawQuery(query,null);
        }
        return cursor;
    }

    public long updatePersonData(String row_id,String name, String surname, String gender, String dateOfBirth,String country,String bloodType,String rhFactor, String allergies,String medicalConditions){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(PERSON_COLUMN_NAME,name);
        cv.put(PERSON_COLUMN_SURNAME,surname);
        cv.put(PERSON_COLUMN_GENDER,gender);
        cv.put(PERSON_COLUMN_DATE_OF_BIRTH, dateOfBirth);
        cv.put(PERSON_COLUMN_COUNTRY,country);
        cv.put(PERSON_COLUMN_BLOOD_TYPE,bloodType);
        cv.put(PERSON_COLUMN_RH_FACTOR,rhFactor);
        cv.put(PERSON_COLUMN_ALLERGIES,allergies);
        cv.put(PERSON_COLUMN_MEDICAL_CONDITIONS,medicalConditions);

        return db.update(PERSON_TABLE_NAME,cv,"_id_person=?",new String[]{row_id});
    }

    public long deletePersonByID(String row_id){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(PERSON_TABLE_NAME,"_id_person=?",new String []{row_id});
    }

    public void deleteAllPersons(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM " + PERSON_TABLE_NAME);
    }
}
