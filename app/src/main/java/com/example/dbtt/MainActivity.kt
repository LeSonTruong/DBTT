package com.example.dbtt

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tvWeather: TextView
    private val apiKey = BuildConfig.WEATHER_API_KEY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("DEBUG_API", "API Key: ${BuildConfig.WEATHER_API_KEY}") // Log API Key
        tvWeather = findViewById(R.id.tvWeather)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestLocationPermission()
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    @SuppressLint("SetTextI18n")
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getLastLocation()
            } else {
                tvWeather.text = "Quyền vị trí bị từ chối!"
            }
        }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                fetchWeather(it.latitude, it.longitude)
            } ?: run {
                tvWeather.text = "Không thể lấy vị trí!"
            }
        }
    }

    private fun fetchWeather(lat: Double, lon: Double) {
        RetrofitInstance.api.getWeather(lat, lon, apiKey).enqueue(object : Callback<WeatherResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { weatherResponse ->
                        val currentTemp = weatherResponse.main.temp
                        val condition = weatherResponse.weather[0].description
                        tvWeather.text = "Nhiệt độ: $currentTemp°C\nTrạng thái: $condition"
                    }
                } else {
                    tvWeather.text = "Lỗi khi lấy dữ liệu!"
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                tvWeather.text = "Lỗi kết nối!"
            }
        })
    }
}

