package ddwucom.mobile.finalreport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ddwucom.mobile.finalreport.databinding.ActivityDeveloperBinding

class DeveloperActivity : AppCompatActivity() {
    lateinit var developerBinding : ActivityDeveloperBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        developerBinding = ActivityDeveloperBinding.inflate(layoutInflater)
        setContentView(developerBinding.root)
        val dto = intent.getSerializableExtra("dateDto") as DateDto

        developerBinding.checkTime.setText(dto.toString())

    }
}