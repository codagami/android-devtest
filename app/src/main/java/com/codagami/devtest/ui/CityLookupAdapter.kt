package com.codagami.devtest.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codagami.devtest.R
import com.codagami.devtest.models.api.CityLookupResponse

/**
 * View adapter for city lookups, provided as a drop-down below the search box
 */
class CityLookupAdapter(private val cityList: MutableList<CityLookupResponse.CityLookupResponseItem>, private val onClick: (item: CityLookupResponse.CityLookupResponseItem) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CityViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.city_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is CityViewHolder) {
            holder.bind(cityList[position], onClick)
        }
    }

    override fun getItemCount(): Int = cityList.size

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var cityName = itemView.findViewById<TextView>(R.id.city_name_text)

        fun bind(city: CityLookupResponse.CityLookupResponseItem?, onClick: (item: CityLookupResponse.CityLookupResponseItem) -> Unit) {
            if(city != null) {
                val name = "${city.name}, ${city.state}, ${city.country}"
                cityName.text = name
                cityName.setOnClickListener { onClick.invoke(city) }
            }
        }
    }
}