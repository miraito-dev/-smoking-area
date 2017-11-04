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



class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private val mMakerArray: MutableList<Marker> = mutableListOf<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // 検索用ボタンのフラグメント
        val selectTypeFragment = supportFragmentManager.findFragmentById(R.id.select_type)
        val buttonList: MutableList<CompoundButton> = selectTypeFragment.getButton
        // 取得した要素の子要素を順に取り出す
        for (i in 0 until buttonList.count()){
            val button: CompoundButton = buttonList.get(i)
            // ボタンの状態を取得
            if(!button.isChecked){
                // onの状態ではない場合は次へ
                continue
            }
            // onになっているボタンの種類を判別
            Log.d("inamura", button.id.toString())
        }
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
        initGoogleMapUi(mMap)
    }

    private fun initGoogleMapUi(mMap: GoogleMap)
    {
        mMap.isMyLocationEnabled = true

        // 現在位置を移動してズームも変更
        val latLnd: LatLng = LatLng(35.685175, 139.7528)
        val cuMove: CameraUpdate = CameraUpdateFactory.newLatLngZoom(latLnd, 14.0f)
        mMap.moveCamera(cuMove)

        // ズームボタンも表示
        mMap.uiSettings.isZoomControlsEnabled = true

        // UIが被らないようにPaddingを設定
        mMap.setPadding(0, 100, 0, 100)

        mMap.setOnInfoWindowClickListener{ marker : Marker ->
            val intent = Intent(this, PlaceDetail::class.java)
            val placeDao = PlaceDao(1, marker.title, "", marker.position.latitude, marker.position.longitude);

            intent.putExtra("place_dao", placeDao)
            startActivity(intent)
        }
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

                runOnUiThread {
                    // アイコンを追加し、マーカー管理用のリストに追加
                    mMakerArray.add(googleMap!!.addMarker(mOption))
                }

            }
        } catch (e: Exception) {
            Log.d("error", e.message)
        }
    }

    private fun clearIcon(){
        for (i in 0 until mMakerArray.count()){
            // 順に取り出して削除
            mMakerArray.get(i).remove()
        }
    }
}
