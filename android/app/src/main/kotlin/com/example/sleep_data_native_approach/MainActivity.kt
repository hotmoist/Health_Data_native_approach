package com.example.sleep_data_native_approach

import android.content.Intent
import android.health.connect.datatypes.SleepSessionRecord
import android.health.connect.datatypes.StepsRecord
import android.net.Uri
import android.os.Build
import com.google.android.libraries.healthdata.data.*

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.RequiresApi
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.SleepStageRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.platform.client.permission.Permission
import com.google.android.libraries.healthdata.HealthDataClient

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

class MainActivity: FlutterFragmentActivity() {
    private val CHANNEL = "com.example.sleep_data_native_approach/health/test1"
    val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()
    val requestPermissions = registerForActivityResult(requestPermissionActivityContract) {granted ->
        if (granted.containsAll(PERMISSIONS)) {
            // permission granted
        } else {
            // lack of permission
        }
    }
    val PERMISSIONS =
        setOf(
            HealthPermission.getReadPermission(androidx.health.connect.client.records.StepsRecord::class),
            HealthPermission.getWritePermission(androidx.health.connect.client.records.StepsRecord::class),
            HealthPermission.getReadPermission(androidx.health.connect.client.records.SleepSessionRecord::class),
        )

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "getHealthData" -> {
//                    CoroutineScope(Dispatchers.IO).launch {
//                        val healthData = fetchHealthData(result)
//                        val healthData = test(result)
//                        withContext(Dispatchers.Main) {
//                            result.success(healthData)
                            fetchHealthData(result)
//                        }
//                    }
                }
                else -> result.notImplemented()
            }
        }
    }

    private fun fetchHealthData(result: MethodChannel.Result) {
        val healthConnectClient = HealthConnectClient.getOrCreate(applicationContext)

        CoroutineScope(Dispatchers.IO).launch {
            try {
//                val requestPermissions = registerForActivityResult(requestPermissionActivityContract) {granted ->
//                    if (granted.containsAll(PERMISSIONS)) {
//                        // permission granted
//                    } else {
//                        // lack of permission
//                    }
//                }
                val granted = healthConnectClient.permissionController.getGrantedPermissions()
                if(granted.containsAll(PERMISSIONS)){

//                    val sleepResponse = healthConnectClient.aggregate(
//                        AggregateRequest(
//                            metrics = setOf(androidx.health.connect.client.records.SleepSessionRecord.SLEEP_DURATION_TOTAL),
//                            timeRangeFilter = TimeRangeFilter.Companion.between(Instant.now().minus(Duration.ofDays(7)), Instant.now())
//                        )
//                    )
                    val endTime = Instant.now()
                    // 현재 로컬 날짜 및 시간으로 변환 (예: 시스템 기본 시간대를 사용)
                    val currentLocalDateTime = endTime.atZone(ZoneId.systemDefault())

                    // 오늘 자정의 로컬 날짜 및 시간을 구합니다.
                    val startOfTodayLocalDateTime = currentLocalDateTime.toLocalDate().atStartOfDay(ZoneId.systemDefault())

                    // 오늘 자정의 로컬 날짜 및 시간을 다시 Instant로 변환합니다.
                    val startTime = startOfTodayLocalDateTime.toInstant()

                    val response = healthConnectClient.aggregate(
                        AggregateRequest(
                            metrics = setOf(androidx.health.connect.client.records.StepsRecord.COUNT_TOTAL),
                            timeRangeFilter = androidx.health.connect.client.time.TimeRangeFilter.Companion.between(
                                startTime, endTime)
                        )
                    )
                    val sleepData =
                        healthConnectClient.readRecords(
                            ReadRecordsRequest(
                                recordType = androidx.health.connect.client.records.SleepSessionRecord::class,
                                timeRangeFilter = TimeRangeFilter.Companion.between(Instant.now().minus(Duration.ofDays(7)), Instant.now())
                                )
                        )

                    val sleepStages = sleepData.records.filterIsInstance<androidx.health.connect.client.records.SleepSessionRecord>()
                    val sleepStageStrings = sleepStages.map { "${it.startTime} to ${it.endTime}" }

                    val stepCount = response[androidx.health.connect.client.records.StepsRecord.COUNT_TOTAL]


                    withContext(Dispatchers.Main){
                        result.success(sleepStageStrings.toString() + "\nstep count: " + stepCount)
                    }
                }else{
                    requestPermissions.launch(PERMISSIONS)
                }


            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    result.error("ERROR_FETCHING_DATA", e.message, null)
                }
            }
        }
    }

}
