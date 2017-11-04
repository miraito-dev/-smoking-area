package jp.co.miraito_inc.smokernavi

import android.content.Intent
import android.os.AsyncTask
import android.support.v4.app.FragmentActivity
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions



class MapsActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
    }
}
