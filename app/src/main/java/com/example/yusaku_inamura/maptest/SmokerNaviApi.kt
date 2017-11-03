package com.example.yusaku_inamura.maptest
import android.util.Log
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException

/**
 * Created by yusaku_inamura on 2017/11/03.
 */
class SmokerNaviApi (){

    init {
        Log.d("SmokerNaviApi", "初期化を行いました")
    }

    // 喫煙場所情報を取得する
    public fun getPlaces(condition: MutableList<String>): MutableList<PlaceDao>{
        // http接続用クライアントを初期化
        val client = initHttpClient()
        // APIにアクセス
        val places: MutableList<PlaceDao> = AccessApi(client)

        return places
    }

    // Http接続用クライアントを初期化して返す
    private fun initHttpClient(): OkHttpClient{
        val sslContext = SSLContext.getInstance("SSL")
        // MyX509TrustManager を割り当てる
        // MyX509TrustManager は全ての証明書を許可するようにしている
        sslContext.init(null, arrayOf<TrustManager>(MyX509TrustManager()), java.security.SecureRandom())
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.sslSocketFactory(sslContext.getSocketFactory())

        return clientBuilder.build()
    }

    // APIにアクセスし、結果を受け取る
    private fun AccessApi(client: OkHttpClient): MutableList<PlaceDao>{
        val places: MutableList<PlaceDao> = mutableListOf<PlaceDao>()
        val request = Request.Builder()
                .url("https://dev.smokernavi.dnsalias.net/api/mapNoLogin/search?")
                .get()
                .build()
        // APIアクセス
        val response: Response = client.newCall(request).execute()
        // レスポンス返却時の処理
        var res = response.body()!!.string()
        val jsons = JSONArray(res)
        // 取得したデータをループ
        for (i in 0 until jsons.length()) {
            val json = jsons.getJSONObject(i)
            val place = PlaceDao(json.getInt("id"), json.getString("name"), json.getString("description"), json.getDouble("latitude"), json.getDouble("longitude"))
            places.add(place)
        }

        return places

    }

    internal class MyX509TrustManager : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }

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