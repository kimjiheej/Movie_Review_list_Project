package ddwucom.mobile.finalreport

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class MovieDBHelper(context: Context?) : SQLiteOpenHelper(context,DB_NAME,null,1) { // 생성자에 값을 전달하였음

    val TAG = "MovieDBHelper"
    //vieDto (val photo : Int, val director : String, val movieTitle : String, val rate : Int, val genre : String){

    companion object { // 정적 변수가 만들어지게 된다
        const val DB_NAME = "movie_db"
        const val TABLE_NAME = "movie_table"
        const val COL_DIRECTOR = "director"
        const val COL_MOVIETITLE = "movieTitle"
        const val COL_RATE = "rate"
        const val COL_GENRE = "genre"
        const val COL_PHOTO = "photo"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME ( ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COL_PHOTO INTEGER, $COL_DIRECTOR TEXT, $COL_MOVIETITLE TEXT, $COL_RATE TEXT, $COL_GENRE TEXT )"

        db?.execSQL(CREATE_TABLE)

        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL, ${R.drawable.one}, '존', '아이언맨', '5', '액션')")
        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL, ${R.drawable.two},  '맷 리브스', '베트맨', '7', '슈퍼히어로')")
        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL,  ${R.drawable.three},'이안', '헐크', '8', '액션')")
        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL, ${R.drawable.four}, '패티', '원더우먼', '10', '첩보')")
        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL, ${R.drawable.four}, '박은형', '마음이', '10', '감동')")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS ${TABLE_NAME}"
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }
}