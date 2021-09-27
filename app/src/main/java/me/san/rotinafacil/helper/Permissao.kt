package me.san.rotinafacil.helper

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build

class Permissao {
    companion object {
        fun validarPermissoes(mActivity: Activity): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return if (mActivity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || mActivity.checkSelfPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    mActivity.requestPermissions(permission, Constants.CODES.PERMISSION_CODE)
                    true
                } else {
                    //permission already granted
                    false
                }
            }
            return false
        }
    }
}