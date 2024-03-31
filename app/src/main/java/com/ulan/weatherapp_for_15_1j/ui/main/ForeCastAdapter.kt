package com.ulan.weatherapp_for_15_1j.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ulan.weatherapp_for_15_1j.data.model.ForecastDayDto
import com.ulan.weatherapp_for_15_1j.databinding.ItemForeCastWeatherBinding
import com.ulan.weatherapp_for_15_1j.ui.loadImage
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class ForeCastAdapter :
    ListAdapter<ForecastDayDto, ForeCastAdapter.ForeCastViewHolder>(ForecastDiffUtil()) {

    class ForeCastViewHolder(private val binding: ItemForeCastWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(forecastModel: ForecastDayDto?) {
            binding.itemTemp.text = "${forecastModel?.day?.foreCastTemp}"
            binding.itemImg.loadImage("https:${forecastModel?.day?.condition?.icon}?")
            val unixTimeTemp = forecastModel?.dateEpoch
            binding.itemDay.text = unixTimeTemp?.toLong()?.let { formatterUnixTimestamp(it) }

            binding.maxTemp.text = "${forecastModel?.day?.maxTempC}"
            binding.minTemp.text = "${forecastModel?.day?.minTempC}"
        }

        private fun formatterUnixTimestamp(unixTimeTemp: Long): String {
            val instant =Instant.ofEpochSecond(unixTimeTemp)
            val formatter = DateTimeFormatter.ofPattern("EEE",Locale.ENGLISH)
                .withZone(ZoneId.systemDefault())


            return formatter.format(instant)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForeCastViewHolder {
        val binding =
            ItemForeCastWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForeCastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForeCastViewHolder, position: Int) {
        val forecastModel = getItem(position)
        holder.onBind(forecastModel)
    }

    class ForecastDiffUtil : DiffUtil.ItemCallback<ForecastDayDto>() {
        override fun areItemsTheSame(oldItem: ForecastDayDto, newItem: ForecastDayDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ForecastDayDto, newItem: ForecastDayDto): Boolean {
            return oldItem == newItem
        }
    }
}
