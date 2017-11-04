package jp.co.miraito_inc.smokernavi

import android.app.Fragment
import android.app.FragmentContainer
// もしapiレベル16以下に対応させたい場合はこれをimportするといいらしい
//import android.support.v4.app.Fragment;
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout

/**
 * Created by yusaku_inamura on 2017/11/04.
 */
public class SelectTypeFragment(): Fragment() {
    companion object {
        fun getInstance(): SelectTypeFragment{
            return SelectTypeFragment()
        }
    }
    // Fragmentで表示するViewを作成する
    override public fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        super.onCreateView(inflater, container, savedInstanceState)
        // フラグメント用のViewを作成
        return inflater.inflate(R.layout.fragment_select_type, container, false)
    }
    // Viewが生成完了した時点で呼ばれるメソッド
    override public  fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    // 現在のボタンの状態を取得し、テキストにして返却する
    public fun retType(separator: String): String{
        var typeStr: String = ""
        // 取得対象となるボタンの一覧を取得
        val buttonList: MutableList<CompoundButton> = getButton()
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
        return typeStr
    }

    private fun setEventListner(buttonList: MutableList<CompoundButton>){
        // 取得した要素の子要素を順に取り出す
        for (i in 0 until buttonList.count()){
            val button: CompoundButton = buttonList.get(i)
            button.setOnClickListener {
                val types: MutableList<String> = mutableListOf<String>("1")
                val places: MutableList<PlaceDao> = SmokerNaviApi().getPlaces(types)
                (parentFragment as TypeButtonCallback).typeButtonClick(places)
            }
        }
    }

    public fun getButton(): MutableList<CompoundButton>{
        val buttonList: MutableList<CompoundButton> = mutableListOf<CompoundButton>()
        // ボタンが配置されているエリアを取得
        val buttonLayout: LinearLayout = view.findViewById(R.id.button_area) as LinearLayout
        val childCount:Int = buttonLayout.childCount
        // 取得した要素の子要素を順に取り出す
        for (i in 0 until childCount){
            val child: View = buttonLayout.getChildAt(i)
            // 対象となる型かどうかチェック(CompoundButtonはon,offなど二つの状態を保つViewの抽象クラス)
            if (child is CompoundButton){
                // 対象となる型の場合は返却用のリストに追加
                buttonList.add(child)
            }
        }
        return buttonList
    }
}