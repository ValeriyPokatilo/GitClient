package app.xl.androidapp.presentation.repositories

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import app.xl.androidapp.databinding.ViewPlaceholderBinding
import app.xl.androidapp.presentation.models.PlaceholderModel

class PlaceholderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewPlaceholderBinding = ViewPlaceholderBinding.inflate(
        LayoutInflater.from(context),  this
    )

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
    }

    fun show(model: PlaceholderModel) {
        visibility = View.VISIBLE

        with(binding) {
            placeholderIcon.setImageResource(model.iconRes)

            placeholderTitle.apply {
                text = model.title
                setTextColor(ContextCompat.getColor(context, model.titleColorRes))
            }

            placeholderMessage.text = model.message

            button.apply {
                text = model.buttonTitle
                setOnClickListener { model.buttonAction() }
                visibility = View.VISIBLE
            }
        }
    }

    fun hide() {
        visibility = View.GONE
    }
}