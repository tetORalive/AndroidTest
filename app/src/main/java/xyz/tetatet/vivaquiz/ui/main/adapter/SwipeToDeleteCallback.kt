package xyz.tetatet.vivaquiz.ui.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.Color.parseColor
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import xyz.tetatet.vivaquiz.R
import java.util.*
import kotlin.math.abs


abstract class SwipeToDeleteCallback(
    context: Context,
    @DrawableRes iconId: Int,
    dragDirs: Int = 0,
    swipeDirs: Int = ItemTouchHelper.LEFT
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    companion object {
        const val BUTTON_WIDTH = 200
    }

    private var deleteIcon: Drawable = ContextCompat.getDrawable(context, iconId)!!
    private var background: Drawable = ContextCompat.getDrawable(context, R.drawable.bgr_rect_corners)!!

    private lateinit var recyclerView: RecyclerView
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    private var swipedPos = -1


    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }
    @Synchronized
    fun recoverSwipedItem() {
        recoverQueue = ArrayDeque(recoverQueue.distinct())
        while (!recoverQueue.isEmpty()) {
            val pos: Int? = recoverQueue.poll()
            if (pos != null) {
                if (pos > -1) {
                    recyclerView.adapter?.notifyItemChanged(pos)
                }
            }
        }
    }


    private val onTouchListener: View.OnTouchListener = object : View.OnTouchListener {
        override fun onTouch(view: View?, e: MotionEvent): Boolean {
            if (swipedPos < 0) return false
            val point = Point(e.rawX.toInt(), e.rawY.toInt())
            val swipedViewHolder = recyclerView.findViewHolderForAdapterPosition(swipedPos)
            val swipedItem: View? = swipedViewHolder?.itemView
            val rect = Rect()
            swipedItem?.getGlobalVisibleRect(rect)
            if (e.action == MotionEvent.ACTION_UP) {
                if (rect.top < point.y && rect.bottom > point.y)  {
                    if (background.bounds.left<point.x && background.bounds.right>point.x) {
                        onDelete(swipedPos)
                        swipedPos = -1
                    }
                } else {
                    recoverQueue.add(swipedPos)
                    swipedPos = -1
                    recoverSwipedItem()
                }
            }
            return false
        }
    }
    override fun getSwipeVelocityThreshold(defaultValue: Float): Float = 15f * defaultValue

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float = 0.1f * defaultValue

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float = 0.5f * BUTTON_WIDTH

    override fun isLongPressDragEnabled(): Boolean = false

    private var recoverQueue = ArrayDeque<Int>()

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition
        if (swipedPos != pos ) {
            recoverQueue.add(swipedPos)
            swipedPos = pos
            recoverSwipedItem()
        }
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
       viewHolder?.adapterPosition

    }


    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val itemView = viewHolder.itemView
        val iconMarginVertical = (viewHolder.itemView.height - deleteIcon.intrinsicHeight) / 2
        var translationX = dX

        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        val left = itemView.right - BUTTON_WIDTH
        val right = left + BUTTON_WIDTH
        val top = itemView.top
        val bottom = itemView.bottom
        //Comments -> Calculate position of delete icon
        val deleteIconTop = top + itemHeight.minus(deleteIcon.intrinsicHeight)/ 2
        val deleteIconBottom = deleteIconTop.plus(deleteIcon.intrinsicHeight)
        val deleteIconMargin = deleteIcon.intrinsicHeight
        val deleteIconLeft = left + abs(right - left) / 2 - deleteIcon.intrinsicWidth / 2
        val deleteIconRight = deleteIconLeft.plus(deleteIcon.intrinsicWidth)


        if (dX < 0 ) {
            deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)

            background.setBounds(left, top, right, bottom)


            deleteIcon.level = 0
            translationX = dX * BUTTON_WIDTH / itemView.width

            c.restore()
            background.draw(c)
            deleteIcon.draw(c)
        }
        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun attachToMyRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        this.recyclerView.setOnTouchListener(onTouchListener)
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(this.recyclerView)
    }

    open fun onClearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }

    abstract fun onDelete(position:Int)
}