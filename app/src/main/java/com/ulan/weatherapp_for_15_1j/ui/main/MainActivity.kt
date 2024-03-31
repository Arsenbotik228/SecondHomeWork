package com.ulan.weatherapp_for_15_1j.ui.main

import android.os.Bundle
import android.util.Log
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
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SearchBottomSheetFragment.SendText {
    private lateinit var binding: ActivityMainBinding
    private val adapter : ForeCastAdapter by lazy { ForeCastAdapter() }
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
        binding.rvForecast.adapter = adapter

        viewModel.liveData.observe(this) {
            binding.btnChangeCountry.text = "${it.location.country}, ${it.location.name}"
            binding.tvTemp.text = it.current.tempC.toString()
            binding.tvCondition.text = getFirstTwoWords(it.current.condition.text)
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
            adapter.submitList(it.forecast.forecastDay)

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
        val c: Calendar = Calendar.getInstance()
        val formatterr = SimpleDateFormat("dd-MM-yyyy (EEEE)")

        for (i in 0..9) {
            c.add(Calendar.DATE, 1)
            Log.d("tag", formatterr.format(c.getTime()))
        }

        val zoneDateTime = instant.atZone(ZoneId.of(zoneId))
        val formatter = DateTimeFormatter.ofPattern("HH'h' mm'm'", Locale.ENGLISH)
        return formatter.format(zoneDateTime)
    }
    private fun getFirstTwoWords(fullText: String): String {
        val words = fullText.split("")
        return if (words.size >= 2) {
            "${words[0]} ${words[1]}"
        } else {
            fullText
        }
    }

}
