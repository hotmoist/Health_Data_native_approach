package com.example.sleep_data_native_approach

import android.health.connect.ReadRecordsRequest
import android.health.connect.TimeRangeFilter
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateRequest

import com.google.android.libraries.healthdata.*

import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.time.Instant

class ViewPermissionUsageActivity: FlutterFragmentActivity() {

//    private val CHANNEL = "com.example.sleep_data_native_approach/health/test1"
//    val PERMISSIONS =
//        setOf(
//            HealthPermission.getReadPermission(StepsRecord::class),
//            HealthPermission.getWritePermission(StepsRecord::class)
//        )
//    val healthConnectClient = HealthConnectClient.getOrCreate(this.applicationContext)
//
//    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
//        super.configureFlutterEngine(flutterEngine)
//        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
//            when (call.method) {
//                "getHealthData" -> {
//                    CoroutineScope(Dispatchers.IO).launch {
//                        val healthData = fetchHealthData(result)
////                        val healthData = test(result)
//                        withContext(Dispatchers.Main) {
//                            result.success(healthData)
//                        }
//                    }
//                }
//                else -> result.notImplemented()
//            }
//        }
//    }
//
//    private fun test(result: MethodChannel.Result):String {
//        return "test"
//    }
//
//    private fun fetchHealthData(result: MethodChannel.Result):String {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()
//                val requestPermissions = registerForActivityResult(requestPermissionActivityContract) {granted ->
//                    if (granted.containsAll(PERMISSIONS)) {
//                        // permission granted
//                    } else {
//                        // lack of permission
//                    }
//                }
//                val granted = healthConnectClient.permissionController.getGrantedPermissions()
//                if(granted.containsAll(PERMISSIONS)){
//                    val response = healthConnectClient.aggregate(
//                        AggregateRequest(
//                            metrics = setOf(StepsRecord.COUNT_TOTAL),
//                            timeRangeFilter = androidx.health.connect.client.time.TimeRangeFilter.Companion.between(
//                                Instant.MIN, Instant.MAX)
//                        )
//                    )
//                    val stepCount = response[StepsRecord.COUNT_TOTAL]
//                    withContext(Dispatchers.Main){
//                        result.success(stepCount.toString())
//                    }
//                }else{
//                    requestPermissions.launch(PERMISSIONS)
//                }
//
//
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main){
//                    result.error("ERROR_FETCHING_DATA", e.message, null)
//                }
//            }
//        }
//        return "not implemented"
//    }
}