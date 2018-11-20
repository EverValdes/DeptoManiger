package controller

import adapter.ItemRecyclerDataAdapter
import android.graphics.*
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper.Callback
import android.view.MotionEvent
import android.view.View
import android.support.v7.widget.helper.ItemTouchHelper.*

class SwipeController: Callback {

    var swipeBack = false
    var buttonShowedState = ButtonsState.GONE
    lateinit var buttonInstance: RectF
    var currentItemViewHolder: ItemRecyclerDataAdapter.ItemViewHolder? = null
    var buttonsActions: SwipeControllerActions
    val buttonWidth: Float = 300F

    constructor(buttonsActions: SwipeControllerActions) {
        this.buttonsActions = buttonsActions
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(0, LEFT or RIGHT)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {

    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.GONE
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dXImput: Float, dYImput: Float, actionState: Int, isCurrentlyActive: Boolean) {
        //as for kotlin 5.1 method parameter are not longer var, they are implicity val and there is no way to specify them as var. Kotlin does no t support passing parameter by reference
        var dX = dXImput
        var dY = dYImput
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.GONE) {
                if (buttonShowedState == ButtonsState.LEFT_VISIBLE) dX = Math.max(dX, buttonWidth)
                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) dX = Math.min(dX, -buttonWidth)
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
            else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

            if (buttonShowedState == ButtonsState.GONE) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
            currentItemViewHolder = viewHolder as ItemRecyclerDataAdapter.ItemViewHolder
        }
    }

    fun setTouchListener(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        recyclerView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                swipeBack = event!!.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP
                if (swipeBack) {
                    if (dX < -buttonWidth) buttonShowedState = ButtonsState.RIGHT_VISIBLE
                    else if (dX > buttonWidth) buttonShowedState  = ButtonsState.LEFT_VISIBLE

                    if (buttonShowedState != ButtonsState.GONE) {
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        setItemsClickable(recyclerView, false)
                    }
                }
                return false
            }
        })
    }

    fun setTouchDownListener(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        recyclerView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event!!.getAction() == MotionEvent.ACTION_DOWN) {
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
                return false
            }
        })
    }

    fun setTouchUpListener(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        recyclerView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event!!.getAction() == MotionEvent.ACTION_UP) {
                    super@SwipeController.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        0F,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    recyclerView.setOnTouchListener(object : View.OnTouchListener {
                        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                            return false
                        }
                    })
                    setItemsClickable(recyclerView, true)
                    swipeBack = false

                    if (buttonsActions != null && buttonInstance != null && buttonInstance.contains(
                            event.getX(),
                            event.getY()
                        )
                    ) {
                        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
                            buttonsActions.onEditClicked(viewHolder.getAdapterPosition())
                        } else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
                            buttonsActions.onDeleteClicked(viewHolder.getAdapterPosition())
                        }
                    }
                    buttonShowedState = ButtonsState.GONE
                }
                return false
            }
        })
    }

    fun setItemsClickable(recyclerView: RecyclerView, isClickable: Boolean) {
        for (i in 0..recyclerView.getChildCount()) {
            recyclerView.getChildAt(i)?.let {
                it.isClickable = isClickable
            }
        }
    }

    fun drawButtons(c: Canvas, viewHolder: RecyclerView.ViewHolder) {
        var buttonWidthWithoutPadding= buttonWidth - 20
        var corners= 16
        var itemView= viewHolder.itemView
        var p = Paint()

        var leftButton = RectF(itemView.getLeft().toFloat(), itemView.top.toFloat(), itemView.left + buttonWidthWithoutPadding, itemView.bottom.toFloat())

        p.color= Color.BLUE
        c.drawRoundRect(leftButton, corners.toFloat(), corners.toFloat(), p)
        drawText("Edit", c, leftButton, p)

        var rightButton = RectF(itemView.right.toFloat() - buttonWidthWithoutPadding, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
        p.color = Color.RED
        c.drawRoundRect(rightButton, corners.toFloat(), corners.toFloat(), p)
        drawText("Delete", c, rightButton, p)

        when (buttonShowedState) {
            ButtonsState.LEFT_VISIBLE -> buttonInstance = leftButton
            ButtonsState.RIGHT_VISIBLE -> buttonInstance = rightButton
            ButtonsState.GONE -> {
                val clearPaint = Paint()
                clearPaint.color = 0
                clearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR);
                c.drawRoundRect(rightButton, corners.toFloat(), corners.toFloat(), clearPaint)
            }

        }
    }

    fun drawText(text: String, c: Canvas, button: RectF, p: Paint) {
        var textSize = 60F
        p.color = Color.WHITE
        p.setAntiAlias(true)
        p.textSize = textSize

        var textWidth = p.measureText(text)
        c.drawText(text, button.centerX()-(textWidth/2), button.centerY()+(textSize/2), p)
    }

    fun onDraw(c: Canvas) {
        currentItemViewHolder?.let {
            drawButtons(c, it)
        }
    }
}
