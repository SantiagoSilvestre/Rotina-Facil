package me.san.rotinafacil.ui

import android.app.Activity
import android.widget.Toast

class ToastHelper {

    companion object {
        fun exibirToast(mActivity: Activity, string: String) {
            Toast.makeText(mActivity, string, Toast.LENGTH_SHORT).show()
        }
    }

}