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
        val mapFragment: MapFragment = MapFragment()
        val selectFragment: SelectTypeFragment = SelectTypeFragment()
        transaction.add(R.id.map ,mapFragment, "map")
        transaction.add(R.id.select_type ,selectFragment, "select")
        transaction.commit()
        mapFragment.getMapAsync(this)
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
            val intent = Intent(activity, PlaceDetail::class.java)
            val placeDao = PlaceDao(1, marker.title, "", marker.position.latitude, marker.position.longitude);

            intent.putExtra("place_dao", placeDao)
            startActivity(intent)
        }
    }

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


    override fun typeButtonClick(places: MutableList<PlaceDao>){

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

    private fun clearIcon(){
        for (i in 0 until mMakerArray.count()){
            // 順に取り出して削除
            mMakerArray.get(i).remove()
        }
    }
}