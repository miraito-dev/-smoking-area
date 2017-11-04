package com.example.yusaku_inamura.maptest

import android.app.Fragment
import android.app.FragmentContainer
// もしapiレベル16以下に対応させたい場合はこれをimportするといいらしい
//import android.support.v4.app.Fragment;
import android.os.Bundle
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by yusaku_inamura on 2017/11/04.
 */
class SelectTypeFragment: Fragment() {
    // Fragmentで表示するViewを作成する
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View{
        super.onCreateView(inflater, container, savedInstanceState)
        // フラグメント用のViewを作成
        return inflater.inflate(R.layout.fragment_select_type, container, false)
    }
    // Viewが生成完了した時点で呼ばれるメソッド
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    public fun retType(): String{
        return ""
    }
}