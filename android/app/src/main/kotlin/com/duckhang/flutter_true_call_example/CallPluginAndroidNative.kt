package com.duckhang.flutter_true_call_example

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class CallPluginAndroidNative(private val activity: Activity) : FlutterPlugin, MethodChannel.MethodCallHandler {
    private lateinit var channel: MethodChannel
    private var applicationContext: Context = activity.applicationContext
    private val PERMISSION_REQUEST_CODE = 1

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        applicationContext = binding.applicationContext
        channel = MethodChannel(binding.binaryMessenger, "call_plugin")
        channel.setMethodCallHandler(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        if (call.method == "getIncomingCallNumber") {
            if (checkPermission()) {
                val incomingNumber = getIncomingCallNumber()
                result.success(incomingNumber)
            } else {
                result.error("PERMISSION_DENIED", "Permission denied", null)
            }
        } else {
            result.notImplemented()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkPermission(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.READ_PHONE_STATE
        )

        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(applicationContext, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }

        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getIncomingCallNumber() {
        val permission = Manifest.permission.READ_PHONE_STATE
        val granted = PackageManager.PERMISSION_GRANTED
        if (ContextCompat.checkSelfPermission(applicationContext, permission) == granted) {
            val telephonyManager = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val incomingNumber = telephonyManager.line1Number
            channel.invokeMethod("onIncomingCallNumber", incomingNumber)
        } else {
            requestCallPermissions()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestCallPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.READ_PHONE_STATE
        )

        ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                var allGranted = true
                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        allGranted = false
                        break
                    }
                }
                if (allGranted) {
                    getIncomingCallNumber()
                } else {
                    // Xử lý khi người dùng từ chối cấp quyền
                }
            }
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
