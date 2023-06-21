package ddwucom.mobile.finalreport

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import ddwucom.mobile.finalreport.databinding.ActivityMainBinding

// 과제명 : 영화 정보 관리 앱
// 분반 : 01 분반
// 학번 : 20200961
// 제출일 : 2023.6.21

class MainActivity : AppCompatActivity() {

    lateinit var MovieItemArrayList: ArrayList<MovieDto>
    lateinit var filteredList : ArrayList<MovieDto>


    lateinit var binding: ActivityMainBinding
    var DETAIL_CODE = 100
    lateinit var adapter: MovieAdapter // 어댑터를 멤버 변수로 선언
    lateinit var movies: ArrayList<MovieDto>
    val REQ_ADD = 100
    val REQ_UPDATE = 200
    lateinit var movieDao : MovieDao
    lateinit var dateDto : DateDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }

        movieDao = MovieDao(this)
        movies = movieDao.getAllMovies()
        MovieItemArrayList = movies
        // DB 에서 모든 food를 가져옴
        adapter = MovieAdapter(movies)        // adapter 에 데이터 설정
        binding.recyclerView.adapter = adapter   // RecylcerView 에 adapter 설정


        /*RecyclerView 항목 클릭 시 실행할 객체*/
        /*RecyclerView 항목 클릭 시 실행할 객체*/
        val onClickListener = object : MovieAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                /*클릭 항목의 dto 를 intent에 저장 후 UpdateActivity 실행*/
                val intent = Intent(this@MainActivity, UpdateActivity::class.java)
                intent.putExtra("dto", movies[position])
                startActivityForResult(intent, REQ_UPDATE)
            }
        }

        adapter.setOnItemClickListener(onClickListener)

        val onLongClickListener = object : MovieAdapter.OnItemLongClickListener {
            override fun onItemLongClick(view: View, position: Int) {
                val movieId = movies[position].id

                AlertDialog.Builder(this@MainActivity).run {
                    setTitle("영화 삭제")
                    setMessage("${movies[position].movieTitle}을 삭제하시겠습니까?")
                    setPositiveButton("OK") { dialog, which ->
                        // OK 버튼이 클릭되었을 때의 동작을 여기에 구현합니다.

                        if (movieDao.deleteMovie(movieId.toInt()) > 0) {
                            refreshList(RESULT_OK)
                            Toast.makeText(this@MainActivity, "삭제 완료", Toast.LENGTH_SHORT).show()
                        }
                    }
                    setNegativeButton("Cancel", null) // Cancel 버튼을 눌렀을 때 아무 동작도 하지 않도록 설정합니다.
                    create().show()
                }
            }
        }

        adapter.setOnItemLongClickListener(onLongClickListener)


        binding.searchMovie.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val searchText = binding.searchMovie.text.toString()
                searchFilter(searchText)
            }
        })
    }

    fun searchFilter(searchText: String) {
        filteredList = ArrayList()

        for (i in 0 until MovieItemArrayList.size) {
            val movieTitle = MovieItemArrayList[i].movieTitle
            if (movieTitle.equals(searchText, ignoreCase = true)) {
                filteredList.add(MovieItemArrayList[i])
            }
        }

        adapter.filterList(filteredList)
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_address -> {
                val intent = Intent(this, AddActivity::class.java)
                startActivityForResult(intent, DETAIL_CODE)
            }
            R.id.intro_developer -> {
                val datePickerDialog = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                        // 선택한 날짜 정보를 DateDto 객체에 저장
                        val dateDto = DateDto(p1, p2, p3)

                        // DeveloperActivity로 전달할 Intent 생성
                        val intent = Intent(this@MainActivity, DeveloperActivity::class.java)
                        intent.putExtra("dateDto", dateDto) // DateDto 객체를 Intent에 첨부

                        startActivity(intent) // DeveloperActivity 시작
                    }
                }, 2023,6,20)

                datePickerDialog.show()
            }
            R.id.exit_app -> {
                AlertDialog.Builder(this).run {
                    setTitle("앱 종료")
                    setMessage("앱을 종료하시겠습니까?")
                    setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        finish() // 액티비티를 종료합니다.
                    }
                    setNegativeButton("Cancel", null)
                    show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_UPDATE -> {
                refreshList(resultCode)
            }
            REQ_ADD -> {
                refreshList(resultCode)
            }
        }
    }

    /*다른 액티비티에서 DB 변경 시 DB 내용을 읽어와 Adapter 의 list 에 반영하고 RecyclerView 갱신*/
    private fun refreshList(resultCode: Int) {
        if (resultCode == RESULT_OK) {
            movies.clear() // foods 는 맨 처음에 읽어와서 Adapter 에 넣어준 녀석이다. 원본들이 다 없어진다.
            movies.addAll(movieDao.getAllMovies()) // 몽땅 다 가져와라 DB 에서 다시 읽어와라. 다시 읽어오는 이유는 우리가 수정을 했기 때문이다
            adapter.notifyDataSetChanged()
        } else
            Toast.makeText(this@MainActivity, "취소", Toast.LENGTH_SHORT).show()
    }

}