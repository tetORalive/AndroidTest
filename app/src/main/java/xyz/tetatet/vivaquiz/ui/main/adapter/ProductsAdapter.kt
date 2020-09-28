package xyz.tetatet.vivaquiz.ui.main.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.list_item_product.view.*
import xyz.tetatet.vivaquiz.R
import xyz.tetatet.vivaquiz.io.model.viva.Product
import xyz.tetatet.vivaquiz.ui.base.BaseAdapter

class ProductsAdapter : BaseAdapter<Product?>() {

    /*Constants --------------------------------------------------------------------------*/
    companion object;
    val selectionEvent: PublishSubject<Product> = PublishSubject.create()

    override fun createItemViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = inflate(R.layout.list_item_product, parent)
        return PositionsViewHolder(view)
    }
    inner class PositionsViewHolder(itemView: View) : BaseViewHolder(itemView) {

        @SuppressLint("SetTextI18n", "Range")
        override fun onBindData(item: Product?) {
            itemView.apply {
                productName.text = "${adapterPosition + 1}) ${item?.name}"
                productPrice.text = "${item?.price}"
                Glide.with(this)
                    .load(item?.thumbnail)
                    .centerCrop()
                    .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(productImage)

                setOnClickListener {
                    item?.let { selectionEvent.onNext(it) }
                }
            }
        }
    }
}