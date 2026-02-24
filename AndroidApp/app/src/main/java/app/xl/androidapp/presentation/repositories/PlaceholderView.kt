package app.xl.androidapp.presentation.repositories

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import app.xl.androidapp.R

class PlaceholderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val icon: ImageView
    private val title: TextView
    private val message: TextView

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        LayoutInflater.from(context).inflate(R.layout.view_placeholder, this, true)

        icon = findViewById(R.id.placeholderIcon)
        title = findViewById(R.id.placeholderTitle)
        message = findViewById(R.id.placeholderMessage)
    }

    fun show(
        @DrawableRes iconRes: Int,
        titleText: String,
        messageText: String,
        @ColorRes titleColorRes: Int = android.R.color.white
    ) {
        visibility = View.VISIBLE
        icon.setImageResource(iconRes)
        title.text = titleText
        title.setTextColor(ContextCompat.getColor(context, titleColorRes))
        message.text = messageText
    }

    fun hide() {
        visibility = View.GONE
    }
}