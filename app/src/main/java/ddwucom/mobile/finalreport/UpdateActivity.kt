package ddwucom.mobile.finalreport

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ddwucom.mobile.finalreport.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {

    lateinit var updateBinding : ActivityUpdateBinding
    lateinit var newmovies : MovieDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateBinding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(updateBinding.root)

        /*RecyclerView 에서 선택하여 전달한 dto 를 확인*/
        val dto = intent.getSerializableExtra("dto") as MovieDto



        /*전달받은 값으로 화면에 표시*/
        updateBinding.editGenre.setText(dto.genre)
        updateBinding.editTitle.setText(dto.movieTitle)
        updateBinding.editRate.setText(dto.rate)
        updateBinding.editDirector.setText(dto.director)

        updateBinding.editButton.setOnClickListener {
            val title = updateBinding.editTitle.text.toString()
            val rate = updateBinding.editRate.text.toString()
            val director = updateBinding.editDirector.text.toString()
            val genre = updateBinding.editGenre.text.toString()

            if (title.isEmpty()) {
                Toast.makeText(this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                val newmovies = MovieDto(0, R.drawable.one, director, title, rate, genre)

                if (updateMovie(newmovies) > 0) {
                    setResult(RESULT_OK)
                } else {
                    setResult(RESULT_CANCELED)
                }
                finish()
            }
        }

        updateBinding.cancelButton.setOnClickListener{
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    /*update 정보를 담고 있는 dto 를 전달 받아 dto 의 id 를 기준으로 수정*/
    fun updateMovie(dto: MovieDto): Int {
        val helper = MovieDBHelper(this)
        val db = helper.writableDatabase
        val dto1 = intent.getSerializableExtra("dto") as MovieDto
        val updateValue = ContentValues()
        updateValue.put(MovieDBHelper.COL_MOVIETITLE, dto.movieTitle)
        updateValue.put(MovieDBHelper.COL_RATE,dto.rate)
        updateValue.put(MovieDBHelper.COL_DIRECTOR,dto.director)
        updateValue.put(MovieDBHelper.COL_GENRE, dto.genre)

        val whereCaluse = "${MovieDBHelper.COL_MOVIETITLE}=? AND ${MovieDBHelper.COL_RATE}=? AND ${MovieDBHelper.COL_GENRE}=? AND ${MovieDBHelper.COL_DIRECTOR}=?"
        val whereArgs = arrayOf(dto1.movieTitle,dto1.rate,dto1.genre,dto1.director)
        /*upate 가 적용된 레코드의 개수 반환*/
        val result =  db.update(MovieDBHelper.TABLE_NAME,
            updateValue, whereCaluse, whereArgs)

        helper.close()      // DB 작업 후에는 close()

        return result
    }

}