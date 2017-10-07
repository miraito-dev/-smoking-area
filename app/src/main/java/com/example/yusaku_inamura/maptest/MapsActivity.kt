package com.example.yusaku_inamura.maptest

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.app.FragmentActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.*
import org.json.JSONObject
import org.json.JSONArray
import java.io.IOException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException


class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // テストで変数宣言
        // 文字列
        var a : String = "inamuradesu"
        // 配列
        var hairetu1 = arrayOf(1, 2, 3);
        // ?が型名の後に付いている場合はnullを許容する
        var hairetu2 : Array<Int?> = arrayOfNulls(3)
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
        mMap = googleMap

        // Add a marker in Sydney and move the camera
//        val spot = LatLng(35.814238132208, 139.80168630369)
//        // マーカー用のカスタム画像を作成
//        var iconimg = BitmapDescriptorFactory.fromResource(R.drawable.photo)
//
//        // マーカー設定用オブジェクト生成
//        val mOption = MarkerOptions()
//        // 位置情報設定
//        mOption.position(spot)
//        // アイコンのタイトル設定
//        mOption.title("谷塚駅西口喫煙所")
//        // アイコン画像設定
//        mOption.icon(iconimg)

//        // アイコン追加
//        mMap!!.addMarker(mOption)
//        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(spot))
        search()
    }

    private fun search() {
        // SSL証明書のエラーを無視させる。
        val clientBuilder = OkHttpClient.Builder()

        try {
            val sslContext = SSLContext.getInstance("SSL")

            // MyX509TrustManager を割り当てる
            // MyX509TrustManager は全ての証明書を許可するようにしている
            sslContext.init(null, arrayOf<TrustManager>(MyX509TrustManager()), java.security.SecureRandom())
            clientBuilder.sslSocketFactory(sslContext.getSocketFactory())
        } catch (e: Exception) {
            Log.d("inamura", "SSL Setting Error")
        }


        val request = Request.Builder()
                .url("https://dev.smokernavi.dnsalias.net/api/mapNoLogin/search")
                .get()
                .build()

        val client = clientBuilder.build()

        client.newCall(request).enqueue(object : Callback {
            override
            fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Log.d("error", e.message)
            }

            @Throws(IOException::class)
            override
            fun onResponse(call: Call, response: Response) {
                var res = response.body()!!.string()
                try {
                    val jsons = JSONArray(res)

                    for (i in 0 until jsons.length()) {
                        val json = jsons.getJSONObject(i)
                        val spot = LatLng(json.getString("latitude").toDouble(), json.getString("longitude").toDouble())
                        // マーカー用のカスタム画像を作成
                        var iconimg = BitmapDescriptorFactory.fromResource(R.drawable.photo)

                        // マーカー設定用オブジェクト生成
                        val mOption = MarkerOptions()
                        // 位置情報設定
                        mOption.position(spot)
                        // アイコンのタイトル設定
                        mOption.title(json.getString("name"))
                        // アイコン画像設定
                        mOption.icon(iconimg)

                        runOnUiThread {
                            // アイコン追加
                            mMap!!.addMarker(mOption)
                        }
//                        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(spot))
//                        Log.d("json name", json.getString("name"))
//                        Log.d("json latitude", json.getString("latitude"))
//                        Log.d("json longitude", json.getString("longitude"))

                    }

                } catch (e: Exception) {
                    Log.d("error", e.message)
                }

            }
        }
        )
    }

    internal class MyX509TrustManager : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
//        val acceptedIssuers: Array<java.security.cert.X509Certificate>
//            get() = arrayOf()

        @Throws(CertificateException::class)
        override
        fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
        }

        @Throws(CertificateException::class)
        override
        fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
        }
    }

}
