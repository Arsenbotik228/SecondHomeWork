package com.ulan.weatherapp_for_15_1j.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.ulan.weatherapp_for_15_1j.R
import com.ulan.weatherapp_for_15_1j.databinding.ActivityMainBinding
import com.ulan.weatherapp_for_15_1j.ui.loadImage
import com.ulan.weatherapp_for_15_1j.ui.search.SearchBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SearchBottomSheetFragment.SendText {
    private lateinit var binding: ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private val viewModel: WeatherViewModel by lazy {
        ViewModelProvider(this)[WeatherViewModel::class.java]
    }
    private var cityName = "Bishkek"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel.getCurrentWeather(cityName)

        viewModel.liveData.observe(this) {
            binding.btnChangeCountry.text = "${it.location.country}, ${it.location.name}"
            binding.tvTemp.text = it.current.tempC.toString()
            binding.tvCondition.text = it.current.condition.text
            binding.imgCondition.loadImage("https:${it.current.condition.icon}")
            binding.tvMaxTemp.text = it.forecast.forecastDay[0].day.maxTempC
            binding.tvMinTemp.text = it.forecast.forecastDay[0].day.minTempC
            binding.tvHumidity.text = "${it.current.humidity}%"
            binding.tvPressure.text = "${it.current.pressuremMb}mBar"
            binding.tvWindInKm.text = "${it.current.windKph}km/h"
            binding.tvSunrise.text = it.forecast.forecastDay[0].astro.sunrise
            binding.tvSunset.text = it.forecast.forecastDay[0].astro.sunset
            binding.tvLocationTime.text =
                formatUnixTimestamp(it.location.localtimeEpoch.toLong(), it.location.zoneId)

        }

        binding.btnChangeCountry.setOnClickListener {
            val bottomSheet = SearchBottomSheetFragment()
            bottomSheet.show(supportFragmentManager, "SearchBottomSheetFragment")
        }
    }

    override fun newText(cityName: String) {

        this.cityName = cityName
        viewModel.getCurrentWeather(cityName)
    }

    private fun formatUnixTimestamp(unixTimestamp: Long, zoneId: String): String {
        val instant = Instant.ofEpochSecond(unixTimestamp)
        val zoneDateTime = instant.atZone(ZoneId.of(zoneId))
        val formatter = DateTimeFormatter.ofPattern("HH'h' mm'm'", Locale.ENGLISH)
        return formatter.format(zoneDateTime)
    }

}
