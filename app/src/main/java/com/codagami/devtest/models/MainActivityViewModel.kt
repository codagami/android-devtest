package com.codagami.devtest.models

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.codagami.devtest.models.api.CityLookupResponse
import com.codagami.devtest.models.api.WeatherForecastResponse

/**
 * Principal view model of the application
 */
class MainActivityViewModel : ViewModel(), Observable {
    @Bindable
    val city = ObservableField<String>()

    val cityList: MutableList<CityLookupResponse.CityLookupResponseItem> = mutableListOf()
    val forecast: MutableList<WeatherForecastResponse.WeatherForecastItem> = mutableListOf()

    val cityWatcher = object : TextWatcher {
        override fun beforeTextChanged(c: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(c: CharSequence?, start: Int, count: Int, after: Int) {
            city.set(c.toString())
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}