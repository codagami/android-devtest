package com.codagami.devtest

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.codagami.devtest.databinding.ActivityMainBinding
import com.codagami.devtest.models.MainActivityViewModel
import com.codagami.devtest.models.api.CityLookupResponse
import com.codagami.devtest.models.api.WeatherForecastResponse
import com.codagami.devtest.services.*
import com.codagami.devtest.ui.CityLookupAdapter
import com.codagami.devtest.ui.ForecastAdapter
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Interface to provide the enable/disable of the indeterminate indicator
 */
interface ToggleIndicatorListener
{
    /**
     * Toggle the state of the indicator
     */
    fun toggleIndicator()
}

class MainActivity : AppCompatActivity(), CityLookupResponseService.CityLookupListener, WeatherForecastRequestService.WeatherForecastListener,
    ToggleIndicatorListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.vm = MainActivityViewModel()

        val cityLookupAdapter = CityLookupAdapter(binding.vm?.cityList!!) { city -> onCityClicked(city) }
        binding.cityLookupListRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cityLookupAdapter
        }

        val weatherForecastAdapter = ForecastAdapter(binding.vm?.forecast!!, this)
        binding.forecastRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = weatherForecastAdapter
        }

        binding.vm?.city?.addOnPropertyChanged { searchForCity() }
        binding.cityEditText.addTextChangedListener(binding.vm?.cityWatcher)
        binding.citySearchButton.setOnClickListener {
            searchForCity()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCityLookup(city: CityLookupResponse.CityLookupResponseItem) {
        try {
            if(binding.cityLookupListRecycler.visibility == GONE) {
                binding.cityLookupListRecycler.adapter?.notifyDataSetChanged()
                binding.cityLookupListRecycler.visibility = VISIBLE
            }

            binding.vm?.cityList?.add(city)
        } catch(e: Exception) {
            Log.e("DEVTEST", "Failed to add city to list: ${e.message}")
        }
    }

    override fun onWeatherForecastReceived(forecast: WeatherForecastResponse.WeatherForecastItem) {
        try {
            binding.vm?.forecast?.add(forecast)
        } catch(e: Exception) {
            Log.e("DEVTEST", "Failed to add weather forecast to list: ${e.message}")
        }
    }

    override fun toggleIndicator() {
        binding.progressIndicator.visibility = GONE
    }

    private fun searchForCity() {
        if(binding.vm?.city?.get() != null) {
            val service = ServiceBuilder.buildService(CityLookupService::class.java)

            let {
                val parts = binding.cityEditText.text.split(',')

                when (parts.size) {
                    3 -> {
                        CityLookupResponseService.getCityLocation(service, parts[0], parts[1], parts[2], this)
                    }
                    2 -> {
                        CityLookupResponseService.getCityLocation(service, parts[0], parts[1], "US", this)
                    }
                    1 -> {
                        CityLookupResponseService.getCityLocation(service, parts[0], "IL", "US", this)
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onCityClicked(city: CityLookupResponse.CityLookupResponseItem) {
        binding.vm?.forecast?.clear()
        binding.forecastRecycler.adapter?.notifyDataSetChanged()

        binding.cityLookupListRecycler.visibility = GONE
        binding.progressIndicator.visibility = VISIBLE

        val service = ServiceBuilder.buildService(WeatherForecastService::class.java)

        let {
            WeatherForecastRequestService.getForecast(service, city.lat, city.lon)
        }
    }
}

fun <T: Observable> T.addOnPropertyChanged(callback: (T) -> Unit): Disposable? =
    object : OnPropertyChangedCallback() {
        override fun onPropertyChanged(observable: Observable?, i: Int) =
            callback(observable as T)
    }.also { addOnPropertyChangedCallback(it) }.let {
        Disposable.fromAction {removeOnPropertyChangedCallback(it)}
    }
