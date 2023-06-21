package ddwucom.mobile.finalreport

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ddwucom.mobile.finalreport.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {

    lateinit var addBinding : ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addBinding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(addBinding.root)

        addBinding.save.setOnClickListener {
            val title = addBinding.addTitle.text.toString()
            val director = addBinding.addDirector.text.toString()
            val rate = addBinding.addRate.text.toString()
            val genre = addBinding.addGenre.text.toString()

            if (title.isEmpty()) {
                Toast.makeText(this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                val newDto = MovieDto(0, R.drawable.one, director, title, rate, genre)      // 화면 값으로 dto 생성, id는 임의의 값 0

                if (addMovie(newDto) > 0) {
                    setResult(RESULT_OK)
                } else {
                    setResult(RESULT_CANCELED)
                }
                finish()
            }
        }
        /*취소버튼 클릭*/
        addBinding.cancel.setOnClickListener{
            setResult(RESULT_CANCELED)
            finish()
        }
    }


    fun addMovie(newDto : MovieDto) : Long  {
        val helper = MovieDBHelper(this)
        val db = helper.writableDatabase

        val newValues = ContentValues()
        newValues.put(MovieDBHelper.COL_MOVIETITLE, newDto.movieTitle)
        newValues.put(MovieDBHelper.COL_PHOTO, newDto.photo)
        newValues.put(MovieDBHelper.COL_RATE, newDto.rate)
        newValues.put(MovieDBHelper.COL_DIRECTOR,newDto.director)
        newValues.put(MovieDBHelper.COL_GENRE,newDto.genre)

        /*insert 한 항목의 id 를 반환*/
        val result = db.insert(MovieDBHelper.TABLE_NAME, null, newValues)

        helper.close()      // DB 작업 후 close() 수행

        return result
    }

}