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
import xyz.tetatet.vivaquiz.io.model.fourquare.Venues
import xyz.tetatet.vivaquiz.ui.base.BaseAdapter

class CategoriesAdapter : BaseAdapter<Venues?>() {

    /*Constants --------------------------------------------------------------------------*/
    companion object {
        const val OLD_STR = "ss3.4sqi.net"
        const val NEW_STR = "foursquare.com"

    }


    val eventClick: PublishSubject<Int> = PublishSubject.create()

    override fun createItemViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        PlacesViewHolder(inflate(R.layout.list_item_category, parent))

    inner class PlacesViewHolder(itemView: View) : BaseViewHolder(itemView) {


        override fun onBindData(item: Venues?) {
            itemView.apply {

                item?.venues?.get(0)?.categories?.get(0)?.let {
                    categoryTitle.text = it.pluralName
                    val imagePath = "${
                        it.icon?.prefix?.replace(OLD_STR, NEW_STR)?.trim()
                    }64${it.icon?.suffix?.trim()}"
                    Glide.with(this)
                        .load(imagePath)
                        .centerCrop()
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(categoryImage)
                }
                item?.let {
                    if (item.venues?.size ?: 0 > 0) {
                        val placesAdapter = PlacesAdapter()
                        placesRecyclerView?.apply { adapter = placesAdapter }
                        placesAdapter.apply {
                            item.venues?.apply { sortBy { it?.location?.distance } }?.let { set(it) }
                        }
                    }
                }
                item?.let {
                    placesRecyclerView.isVisible = it.isExpanded
                    toggleBtn.rotate(if (it.isExpanded) 180f else 0f, 0L).subscribe()
                }
                setOnClickListener {
                    item?.isExpanded?.apply{
                        placesRecyclerView.isVisible = !this
                        item.isExpanded = !this
                        toggleBtn.rotate(if (item.isExpanded) 180f else 0f, 300L).subscribe()
                    }
                }
            }
        }
    }
}