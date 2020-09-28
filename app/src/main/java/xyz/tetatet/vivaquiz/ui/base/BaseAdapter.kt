package  xyz.tetatet.vivaquiz.ui.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import xyz.tetatet.vivaquiz.io.model.fourquare.Venue

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseAdapter<T>.BaseViewHolder> {

    protected var data: MutableList<T?>
    lateinit var context: Context

    constructor() {
        this.data = mutableListOf()
    }

    constructor(data: MutableList<T?>) {
        this.data = data
    }

    abstract fun createItemViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder

    abstract inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun onBindData(item: T?) {}
        open fun onBindData(item: T?, position: Int) { onBindData(item) }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBindData(data[position], position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        createItemViewHolder(parent, viewType)

    protected fun inflate(@LayoutRes id: Int, container: ViewGroup): View {
        context = container.context
        return LayoutInflater.from(context).inflate(id, container, false)
    }

    fun set(data: MutableList<T?>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun set(index: Int, item: T?) {
        this.data[index] = item
        notifyItemChanged(index)
    }

    fun add(data: MutableList<T?>) {
        this.data.addAll(data)
    }

    fun add(position: Int, data: MutableList<T?>) {
        this.data.addAll(position, data)
    }

    fun get(): MutableList<T?> {
        return data
    }

    fun get(index: Int): T? {
        return data[index]
    }

    fun appendData(data: MutableList<T?>) {
        val size = itemCount
        this.data.addAll(data)
        notifyItemRangeInserted(size, itemCount)
    }

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }

    fun addItem(item: T?) {

        data.add(item)
        notifyItemInserted(data.size)
    }

    fun addItem(item: T?, position: Int) {
        data.add(item)
        notifyItemInserted(position)
    }

    fun removeItem(item: T?) {
        val position = data.indexOf(item)
        data.remove(item)
        notifyItemRemoved(position)
    }

    fun removeItem(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
    }

    open fun notifyItemChanged(item: T?) {
        notifyItemChanged(data.indexOf(item))
    }
}
