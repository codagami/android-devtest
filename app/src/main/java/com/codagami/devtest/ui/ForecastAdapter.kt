package com.codagami.devtest.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.codagami.devtest.R
import com.codagami.devtest.ToggleIndicatorListener
import com.codagami.devtest.models.api.WeatherForecastResponse

/**
 * View adapter for the forecast data models
 */
class ForecastAdapter(private val forecastList: MutableList<WeatherForecastResponse.WeatherForecastItem>, private val listener: ToggleIndicatorListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ForecastViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.forecast_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ForecastViewHolder) {
            holder.bind(forecastList[position])
            listener.toggleIndicator()
        }
    }

    override fun getItemCount(): Int = forecastList.size

    class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var icon = itemView.findViewById<AppCompatImageView>(R.id.weather_icon)
        private var description = itemView.findViewById<TextView>(R.id.weather_description)
        private var tempHigh = itemView.findViewById<TextView>(R.id.weather_temp_high)
        private var tempLow = itemView.findViewById<TextView>(R.id.weather_temp_low)

        fun bind(forecast: WeatherForecastResponse.WeatherForecastItem?) {
            if(null != forecast) {
                when (forecast.weather[0].main) {
                    "Clouds" -> {
                        icon.setImageResource(R.drawable.cloudy)
                    }
                    "Rain", "Drizzle", "Thunderstorm" -> {
                        icon.setImageResource(R.drawable.rainy)
                    }
                    "Snow" -> {
                        icon.setImageResource(R.drawable.snowflake)
                    }
                    "Clear" -> {
                        icon.setImageResource(R.drawable.sunny)
                    }
                }

                description.text = forecast.weather[0].description
                tempHigh.text = String.format(itemView.context.getString(R.string.temp_high), forecast.main.tempMax)
                tempLow.text = String.format(itemView.context.getString(R.string.temp_low), forecast.main.tempMin)
            }
        }
    }
}