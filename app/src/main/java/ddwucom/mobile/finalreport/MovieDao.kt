package ddwucom.mobile.finalreport

import android.annotation.SuppressLint
import android.content.Context
import android.provider.BaseColumns

class MovieDao(val context : Context) {

    @SuppressLint("Range")
    fun getAllMovies() : ArrayList<MovieDto> {
        val helper = MovieDBHelper(context)
        val db = helper.readableDatabase

//        val cursor = db.rawQuery("SELECT * FROM ${FoodDBHelper.TABLE_NAME}", null)
        val cursor = db.query(MovieDBHelper.TABLE_NAME, null, null, null, null, null, null)

        val movies = arrayListOf<MovieDto>()

        with (cursor) {
            while (moveToNext()) {
                val id = getLong( getColumnIndex(BaseColumns._ID) )
                val director = getString ( getColumnIndex(MovieDBHelper.COL_DIRECTOR) )
                val movieTitle = getString(getColumnIndex(MovieDBHelper.COL_MOVIETITLE))
                val rate = getString(getColumnIndex(MovieDBHelper.COL_RATE))
                val genre = getString(getColumnIndex(MovieDBHelper.COL_GENRE))
                val photo = getInt(getColumnIndex(MovieDBHelper.COL_PHOTO))
                val dto = MovieDto(id,photo,director,movieTitle,rate,genre)
                movies.add(dto)
            }
        }
        cursor.close()      // cursor 사용을 종료했으므로 close()
        helper.close()      // DB 사용이 끝났으므로 close()
        return movies
    }

    fun deleteMovie(id: Int) : Int {
        val helper = MovieDBHelper(context)
        val db = helper.writableDatabase

        val whereClause = "${BaseColumns._ID}=?"
        val whereArgs = arrayOf(id.toString())

        val result = db.delete(MovieDBHelper.TABLE_NAME, whereClause, whereArgs)

        helper.close()
        return result
    }



}