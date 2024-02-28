
import android.graphics.Color
import android.graphics.Typeface
import android.text.Layout
import android.text.Layout.Alignment
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.AlignmentSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.view.View
import androidx.core.text.HtmlCompat
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.lang.Exception
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * class [GenHtmlCompat] - class for converting html to Spanned interface
 */
class GenHtmlCompat {
    companion object {

        /**
         * method [fromHtml] - for converting html to Spanned interface
         * accepts htmlText and a callback function [onTapLink] to send an event when a link is clicked
         */
        fun fromHtml(htmlText: String, onTapLink: ((url: String) -> Unit)? = null): Spanned {
            return try {
                val document = Jsoup.parse(htmlText)
                var result = SpannableStringBuilder()
                val elements = document.allElements
                elements.forEach { element ->
                    result = result.append(setUpSpanned(element, onTapLink))
                }

                result
            }catch (_ : Exception){
                HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT)
            }
        }

        private fun setUpSpanned(
            element: Element,
            onTapLink: ((url: String) -> Unit)?
        ): SpannableStringBuilder {
            val ownText = element.ownText()
            var result = SpannableStringBuilder()
            if (ownText.isNotEmpty() || ownText.isNotBlank()) {
                val parserResult = ElementParser(element).parse()
                result.append(parserResult.value)
                val length = parserResult.value.length
                if (parserResult.url != null) {
                    result = createSpannableTextWithLink(
                        linkText = parserResult.value,
                        url = parserResult.url,
                        onTapLink = onTapLink,
                    )
                }

                if (parserResult.types.contains(TypeValue.BOLD)) {
                    result.setSpan(
                        StyleSpan(Typeface.BOLD),
                        length - result.length,
                        length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                if (parserResult.types.contains(TypeValue.ITALIC)) {
                    result.setSpan(
                        StyleSpan(Typeface.ITALIC),
                        length - result.length,
                        length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                if (parserResult.types.contains(TypeValue.UNDERLINE)) {
                    result.setSpan(
                        UnderlineSpan(),
                        length - result.length,
                        length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                result.setSpan(
                    AbsoluteSizeSpan(parserResult.textSize.toInt(), true),
                    length - result.length,
                    length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                result.setSpan(
                    ForegroundColorSpan(parserResult.color),
                    length - result.length,
                    length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                result.setSpan(
                    AlignmentSpan.Standard(parserResult.alignment),
                    0,
                    length,
                    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }


            return result
        }

        private fun createSpannableTextWithLink(
            linkText: String,
            url: String,
            onTapLink: ((url: String) -> Unit)?
        ): SpannableStringBuilder {
            val spannableString = SpannableStringBuilder(linkText)
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    onTapLink?.invoke(url)
                }
            }
            spannableString.setSpan(
                clickableSpan,
                0,
                linkText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                URLSpan(url),
                0,
                linkText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return spannableString
        }
    }

    /**
     * [ElementParser] class to convert [Element] library class [Jsoup] to class [ResultValue];
     */
    private class ElementParser(
        private val element: Element
    ) {
        private val bold = "strong"
        private val italic = "em"
        private val underline = "ins"
        private val hrefAttr = "href"
        private var url: String? = null
        private var isList: Boolean = false

        private val types = mutableListOf<TypeValue>()
        private var elementStyle: String? = null
        private var parentElementStyle: String? = null

        /**
         * Entry point to parse [Element];
         */
        fun parse(): ResultValue {
            getTypeValue(element)
            checkLink(element)
            val color: Int = parseColorFromStyleAttribute(elementStyle)
            val textSize = getTextSize(elementStyle)
            var value = element.ownText() + " "
            if(isList){
                value = "\n   ● $value"
            }

            val style  = if(elementStyleHasAlignment) elementStyle else parentElementStyle

            return ResultValue(
                value = value,
                types = types,
                textSize = textSize,
                color = color,
                alignment = parseAlignmentFromStyleAttribute(style),
                url = url
            )
        }

        private fun getTypeValue(element: Element) {
            val tag = element.tagName()
            setUpTypeValue(tag)
            if (tag == "span" || tag == "li" || tag == "p") {
                elementStyle = element.attr("style")
                val parentTagName = element.parent()?.tagName()
                if(tag == "li" || parentTagName == "li"){
                    isList = true
                }
                if(parentTagName == "p"){
                    parentElementStyle = element.parent()?.attr("style")
                }

                return
            }
            val parent = element.parent()
            if (parent != null) {
                getTypeValue(parent)
            }
        }

        private val elementStyleHasAlignment: Boolean
            get() {
                val regex = Regex("text-align:\\s*(\\w+);")
                val matchResult = regex.find(elementStyle ?: "")

                return matchResult != null && matchResult.groupValues.size == 2
            }

        private fun parseAlignmentFromStyleAttribute(style: String?): Alignment {
            if(style == null) return  Alignment.ALIGN_NORMAL
            val regex = Regex("text-align:\\s*(\\w+);")
            val matchResult = regex.find(style)

            return if (matchResult != null && matchResult.groupValues.size == 2) {
                when (matchResult.groupValues[1]) {
                    "left" -> Alignment.ALIGN_NORMAL
                    "center" -> Alignment.ALIGN_CENTER
                    "right" -> Alignment.ALIGN_OPPOSITE
                    else -> Alignment.ALIGN_NORMAL
                }
            } else {
                Alignment.ALIGN_NORMAL
            }
        }

        private fun getTextSize(style: String?): Float {
            val constTextSize = 14f
            if (style.isNullOrEmpty()) return constTextSize
            val pattern: Pattern = Pattern.compile("font-size:\\s*(\\d+)px")

            val startIndex: Int = style.indexOf("font-size: ")
            val endIndex: Int = style.indexOf("px", startIndex)
            if (startIndex != -1 && endIndex != -1) {

                val matcher: Matcher = pattern.matcher(style)
                if (matcher.find()) {
                    return matcher.group(1)?.toFloat() ?: constTextSize
                }
            }

            return constTextSize
        }

        private fun parseColorFromStyleAttribute(style: String?): Int {
            if (style == null) return Color.BLACK
            if (!style.contains("rgb(")) return parseNormalColor(style)
            val regex = Regex("color: rgb\\((\\d+),\\s*(\\d+),\\s*(\\d+)\\)")
            val matchResult = regex.find(style)

            return if (matchResult != null && matchResult.groupValues.size == 4) {
                Color.rgb(
                    matchResult.groupValues[1].toInt(),
                    matchResult.groupValues[2].toInt(),
                    matchResult.groupValues[3].toInt()
                )
            } else {
                Color.BLACK
            }
        }

        private fun parseNormalColor(style: String): Int {
            val colorRegex = Regex("color:[^;]+;")
            val colorMatch = colorRegex.find(style) ?: return Color.BLACK
            val colorCode =
                colorMatch.value.substringAfter("color:").trimEnd(';').replace(" ", "")

            return try {
                Color.parseColor(colorCode)
            } catch (e: IllegalArgumentException) {
                Color.BLACK
            }
        }

        private fun checkLink(element: Element) {
            val tag = element.tagName()
            if(tag == "a"){
                url = element.attr(hrefAttr)
                return
            }
            if (tag == "span") {
                val parent = element.parent()
                if (parent?.tagName() == "a") {
                    url = parent.attr(hrefAttr)
                }
                return
            }
            val parent = element.parent()
            if (parent != null) {
                checkLink(parent)
            }
        }

        private fun setUpTypeValue(tag: String) {
            when (tag) {
                bold -> types.add(TypeValue.BOLD)
                italic -> types.add(TypeValue.ITALIC)
                underline -> types.add(TypeValue.UNDERLINE)
            }
        }
    }

    private class ResultValue(
        val value: String,
        val types: List<TypeValue>,
        val textSize: Float,
        val color: Int,
        val alignment: Layout.Alignment,
        val url: String?
    ) {
        override fun toString(): String {
            return "ResultValue(value='$value', types=$types, textSize=$textSize, color=$color, url=$url)"
        }
    }

    enum class TypeValue {
        BOLD, UNDERLINE, ITALIC
    }
}