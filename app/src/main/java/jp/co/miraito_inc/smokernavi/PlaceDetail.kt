package jp.co.miraito_inc.smokernavi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.gms.location.places.Place
import org.w3c.dom.Text

class PlaceDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        findViewById(R.id.close).setOnClickListener {
            finish()
        }
        val placeDao : PlaceDao = intent.getSerializableExtra("place_dao") as PlaceDao

        val placeName : TextView = findViewById(R.id.placeName) as TextView
        placeName.text = placeDao.name
    }
}
