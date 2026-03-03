package app.xl.androidapp.presentation.utils

import android.content.Context
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import app.xl.androidapp.R
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.core.MarkwonTheme
import org.commonmark.node.Heading

object MarkwonFactory {

    fun createMarkwon(context: Context): Markwon {
        return Markwon.builder(context)
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureTheme(builder: MarkwonTheme.Builder) {
                    val linkColor = ContextCompat.getColor(context, R.color.blue)
                    builder
                        .headingBreakHeight(0)
                        .linkColor(linkColor)
                }

                override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
                    val origin = builder.getFactory(Heading::class.java)
                    builder.setFactory(Heading::class.java) { configuration, props ->
                        arrayOf(
                            origin?.getSpans(configuration, props),
                            ForegroundColorSpan(Color.WHITE)
                        )
                    }
                }
            })
            .build()
    }
}