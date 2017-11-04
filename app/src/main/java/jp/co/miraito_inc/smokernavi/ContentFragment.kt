package jp.co.miraito_inc.smokernavi

import android.app.Fragment
import android.app.FragmentTransaction
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Created by yusaku_inamura on 2017/11/04.
 */
public class ContentFragment: Fragment(), TypeButtonCallback, OnMapReadyCallback{
    private var mMap: GoogleMap? = null
    private val mMakerArray: MutableList<Marker> = mutableListOf<Marker>()

    companion object {
        fun getInstance(): ContentFragment{
            return ContentFragment()
        }
    }
    // Fragmentで表示するViewを作成する
    override public fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        super.onCreateView(inflater, container, savedInstanceState)
        // フラグメント用のViewを作成
        return inflater.inflate(R.layout.fragment_content, container, false)
    }

    override public fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        val mapFragment: Fragment = MapFragment()
        val selectFragment: Fragment = SelectTypeFragment()
        transaction.add(R.id.map ,mapFragment, "map")
        transaction.add(R.id.select_type ,selectFragment, "select")
        transaction.commit()
    }


    override fun typeButtonClick(places: MutableList<PlaceDao>){

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        var mMap = googleMap

        val task = object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                setPlaces(mMap)
                return null
            }
        }
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        mMap.isMyLocationEnabled = true

        // 現在位置を移動してズームも変更
        val latLnd: LatLng = LatLng(35.685175, 139.7528)
        val cuMove: CameraUpdate = CameraUpdateFactory.newLatLngZoom(latLnd, 14.0f)
        mMap.moveCamera(cuMove)

        // ズームボタンも表示
        mMap.uiSettings.isZoomControlsEnabled = true

        // UIが被らないようにPaddingを設定
        mMap.setPadding(0, 100, 0, 100)

        /*
        val listener = object : GoogleMap.OnInfoWindowClickListener {
            override fun onInfoWindowClick(marker : Marker) {
                val name = marker.title
                Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show()
            }
        }
        mMap.setOnInfoWindowClickListener(listener)
        */

        /*
        mMap.setOnInfoWindowClickListener( object : GoogleMap.OnInfoWindowClickListener{
            override fun onInfoWindowClick(marker : Marker) {
                val name = marker.title
                Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show()
            }
        })
        */
    }

    private fun setPlaces(googleMap: GoogleMap) {
        val types: MutableList<String> = mutableListOf<String>()
        val places: MutableList<PlaceDao> = SmokerNaviApi().getPlaces(types)

        try {

            for (i in 0 until places.count()) {
                val place = places.get(i)
                val spot = LatLng(place.latitude, place.longitude)

                // マーカー設定用オブジェクト生成
                val mOption = MarkerOptions()
                // 位置情報設定
                mOption.position(spot)
                // アイコンのタイトル設定
                mOption.title(place.name)
                // マーカー用のカスタム画像を作成
//                var iconimg = BitmapDescriptorFactory.fromResource(R.drawable.photo)
//                // アイコン画像設定
//                mOption.icon(iconimg)

                activity.runOnUiThread {
                    // アイコンを追加し、マーカー管理用のリストに追加
                    mMakerArray.add(googleMap!!.addMarker(mOption))
                }

            }
        } catch (e: Exception) {
            Log.d("error", e.message)
        }
    }
}