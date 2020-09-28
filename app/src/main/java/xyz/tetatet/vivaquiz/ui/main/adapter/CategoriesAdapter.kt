package xyz.tetatet.vivaquiz.ui.main.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.list_item_category.view.*
import xyz.tetatet.vivaquiz.R
import xyz.tetatet.vivaquiz.extensions.rotate
import xyz.tetatet.vivaquiz.io.model.fourquare.Venue
import xyz.tetatet.vivaquiz.ui.base.BaseAdapter

class CategoriesAdapter : BaseAdapter<MutableList<Venue?>?>() {

    /*Constants --------------------------------------------------------------------------*/
    companion object {
        const val OLD_STR = "ss3.4sqi.net"
        const val NEW_STR = "foursquare.com"

    }


    val eventClick: PublishSubject<Int> = PublishSubject.create()

    override fun createItemViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        PlacesViewHolder(inflate(R.layout.list_item_category, parent))

    inner class PlacesViewHolder(itemView: View) : BaseViewHolder(itemView) {

        private var lastExpandedPosition = -1

        override fun onBindData(item: MutableList<Venue?>?) {
            itemView.apply {


                item?.get(0)?.categories?.get(0)?.let {
                    categoryTitle.text = it.pluralName
                    var imagePath = "${it.icon?.prefix?.replace(OLD_STR, NEW_STR)?.trim()}64${it.icon?.suffix?.trim()}"
                    Glide.with(this)
                        .load(imagePath)
                        .centerCrop()
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(categoryImage)
                }

                item?.let {
                    if (item.size > 0) {
                        val placesAdapter = PlacesAdapter()
                        placesRecyclerView?.apply { adapter = placesAdapter }
                        placesAdapter.apply { set(item.apply { sortBy { it?.location?.distance } }) }
                    }
                }
//TODO EXPANDABLE LIST ITEM IMPLEMENTATION
//                placesRecyclerView.isVisible = lastExpandedPosition == adapterPosition
                setOnClickListener {
                    placesRecyclerView.isVisible = !placesRecyclerView.isVisible
                    toggleBtn.rotate(if (placesRecyclerView.isVisible) 180f else 0f, 300L).subscribe()
//                    if (placesRecyclerView.isVisible) {
//                        lastExpandedPosition = adapterPosition
//                        eventClick.onNext(adapterPosition)
//                    } else {
//                        lastExpandedPosition = -1
//                    }
                }
            }
        }
    }
}