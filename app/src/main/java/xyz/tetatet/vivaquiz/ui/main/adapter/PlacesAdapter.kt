package xyz.tetatet.vivaquiz.ui.main.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item_place.view.*
import xyz.tetatet.vivaquiz.R
import xyz.tetatet.vivaquiz.io.model.fourquare.Venue
import xyz.tetatet.vivaquiz.ui.base.BaseAdapter


class PlacesAdapter : BaseAdapter<Venue?>() {

    /*Constants --------------------------------------------------------------------------*/
    companion object;

    override fun createItemViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder = PlacesViewHolder(inflate(
        R.layout.list_item_place, parent))

    inner class PlacesViewHolder(itemView: View) : BaseViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        override fun onBindData(item: Venue?) {
            itemView.apply {
                placeName.text = item?.name?:"Unknown name"
                item?.location?.distance?.toDouble()?.let {
                    val distance = it/1000
                    distanceValue.text = "${String.format("%.2f", distance).replace(".",",")} km"
                }
            }
        }
    }
}