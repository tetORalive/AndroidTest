package  xyz.tetatet.vivaquiz.ui.base

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener


abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseAdapter<T>.BaseViewHolder> {

    protected var data: MutableList<T?>
    lateinit var context: Context
    private var onAttach = true

    constructor() {
        this.data = mutableListOf()
    }

    constructor(data: MutableList<T?>) {
        this.data = data
    }

    abstract fun createItemViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder

    abstract inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init { }
        open fun onBindData(item: T?) {}
        open fun onBindData(item: T?, position: Int) { onBindData(item) }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBindData(data[position], position)
//        setAnimation(holder.itemView, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        createItemViewHolder(
            parent,
            viewType
        )

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                onAttach = false
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }

    open fun setAnimation(itemView: View, position: Int) {
        var i = position
        if (!onAttach) {
            i = -1
        }
        val isNotFirstItem = i == -1
        i++
        itemView.alpha = 0f
        val animatorSet = AnimatorSet()
        val animator = ObjectAnimator.ofFloat(itemView, "alpha", 0f, 0.5f, 1.0f)
        ObjectAnimator.ofFloat(itemView, "alpha", 0f).start()
        animator.startDelay = (if (isNotFirstItem) 500 / 2 else i * 500 / 3).toLong()
        animator.duration = 500
        animatorSet.play(animator)
        animator.start()
    }

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
