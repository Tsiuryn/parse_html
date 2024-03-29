package com.example.parsehtml

import android.graphics.Color
import android.graphics.Typeface
import android.text.Layout
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
import java.util.regex.Matcher
import java.util.regex.Pattern


// Не поддерживает списки (нумерованные и с точками), ссылки, шрифты. Будет отображать через - HtmlCompat.fromHtml

class GenHtml {

    companion object {
        private val unprocessedTags = mutableMapOf(
            "<ul>" to "</ul>",
            "<ol>" to "</ol>",
//            "href" to "http"
        )

        fun fromHtml(htmlText: String, onTapLink: ((url: String) -> Unit)? = null): Spanned {
            try {
                unprocessedTags.keys.forEach { key ->
                    val value = unprocessedTags[key] ?: ""
                    if (htmlText.contains(key) && htmlText.contains(value)) {
                        return HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    }
                }
                val getListSpan = getListSpan(value = htmlText)

                var result = SpannableStringBuilder()

                getListSpan.forEach {
                    result = setUpSpanned(ParserHtmlValue.getValue(it), result, onTapLink)
                }

                setUpAlignText(result, htmlText)

                return result
            } catch (_: Throwable){
                return HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT)
            }
        }

        private fun setUpSpanned(
            value: ResultValue,
            result: SpannableStringBuilder,
            onTapLink: ((url: String) -> Unit)?
        ): SpannableStringBuilder {
            val currentValue = "${value.value} "
            val currentResult = if(value.url != null)
                result.append(createSpannableTextWithLink(linkText = "${value.value} ", url = value.url, onTapLink = onTapLink))
            else
                result.append(currentValue)


            val length = currentResult.length

            if (value.types.contains(TypeValue.BOLD)) {
                currentResult.setSpan(
                    StyleSpan(Typeface.BOLD),
                    length - currentValue.length,
                    length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            if (value.types.contains(TypeValue.ITALIC)) {
                currentResult.setSpan(
                    StyleSpan(Typeface.ITALIC),
                    length - currentValue.length,
                    length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            if (value.types.contains(TypeValue.UNDERLINE)) {
                currentResult.setSpan(
                    UnderlineSpan(),
                    length - currentValue.length,
                    length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            currentResult.setSpan(
                AbsoluteSizeSpan(value.textSize.toInt(), true),
                length - currentValue.length,
                length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            currentResult.setSpan(
                ForegroundColorSpan(value.color),
                length - currentValue.length,
                length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            return currentResult
        }

        private fun createSpannableTextWithLink(linkText: String, url: String, onTapLink: ((url: String) -> Unit)?): SpannableStringBuilder {
            val spannableString = SpannableStringBuilder(linkText)
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    onTapLink?.invoke(url)
                }
            }
            spannableString.setSpan(clickableSpan, 0, linkText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(URLSpan(url), 0, linkText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            return spannableString
        }

        private fun setUpAlignText(
            value: SpannableStringBuilder,
            htmlText: String
        ): SpannableStringBuilder {
            value.setSpan(
                AlignmentSpan.Standard(parseAlignmentFromStyleAttribute(htmlText)),
                0,
                value.length,
                SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return value
        }

        private fun parseAlignmentFromStyleAttribute(htmlText: String): Layout.Alignment {
            val regex = Regex("text-align:\\s*(\\w+);")
            val matchResult = regex.find(htmlText)

            return if (matchResult != null && matchResult.groupValues.size == 2) {
                when (matchResult.groupValues[1]) {
                    "left" -> Layout.Alignment.ALIGN_NORMAL
                    "center" -> Layout.Alignment.ALIGN_CENTER
                    "right" -> Layout.Alignment.ALIGN_OPPOSITE
                    else -> Layout.Alignment.ALIGN_NORMAL
                }
            } else {
                Layout.Alignment.ALIGN_NORMAL
            }
        }

        private fun getListSpan(value: String): List<String> {
            val regex = Regex("<(span|a)\\s[^>]*>(.*?)</(span|a)>")
            val spanMatches = regex.findAll(value)

            val spanList = mutableListOf<String>()

            for (match in spanMatches) {
                spanList.add(match.value)
            }

            return spanList
        }

    }

    private class ParserHtmlValue{
        companion object{
            private val bold = Pair("<strong>", "</strong>")
            private val italic = Pair("<em>", "</em>")
            private val underline = Pair("<ins>", "</ins>")
            private val link = Pair("<a href", ">")

            fun getValue(htmlText: String): ResultValue{
                val listTypes = mutableListOf<TypeValue>()
                var url : String? = null
                var value: String = parseValue(htmlText)
                if(htmlText.startsWith(link.first)){
                    val endTextToParse = link.second
                    val endIndex = htmlText.indexOf(endTextToParse) + 1
                    val urlHtml = htmlText.substring(0, endIndex)
                    url = extractUrlFromHtml(urlHtml)
                    val textWithoutUrl = value.replace(urlHtml, "")
                    value = textWithoutUrl.substring(textWithoutUrl.indexOf(">") + 1, textWithoutUrl.length)
                }
                if (value.contains(bold.first)) {
                    value = value.replace(bold.first, "").replace(bold.second, "")
                    listTypes.add(TypeValue.BOLD)
                }
                if (value.contains(italic.first)) {
                    value = value.replace(italic.first, "").replace(italic.second, "")
                    listTypes.add(TypeValue.ITALIC)
                }
                if (value.contains(underline.first)) {
                    value = value.replace(underline.first, "").replace(underline.second, "")
                    listTypes.add(TypeValue.UNDERLINE)
                }


                return ResultValue(
                    value,
                    types = listTypes,
                    textSize = getTextSize(htmlText),
                    color = parseColorFromStyleAttribute(htmlText),
                    url = url
                )
            }

            private fun extractUrlFromHtml(html: String): String? {
                val startText = "${link.first}=\\\""
                val startIndex = html.indexOf(startText)
                val endIndex = html.indexOf("\" target")
                if(startIndex < 0 || endIndex < 0){
                    return null
                }

                return html.substring(startIndex + startText.length, endIndex - 1)
            }

            private fun parseValue(htmlText: String): String {
                val startTextToParse = ">"
                val endTextToParse = "</span>"
                val startIndex = htmlText.indexOf(startTextToParse) + 1
                val endIndex = htmlText.indexOf(endTextToParse)
                return htmlText.substring(startIndex, endIndex)
            }

            private fun getTextSize(htmlString: String?): Float {
                val constTextSize = 14f
                if (htmlString.isNullOrEmpty()) return constTextSize
                val pattern: Pattern = Pattern.compile("font-size:\\s*(\\d+)px")

                val startIndex: Int = htmlString.indexOf("style=")
                val endIndex: Int = htmlString.indexOf(">", startIndex)
                if (startIndex != -1 && endIndex != -1) {
                    val styleAttribute: String = htmlString.substring(startIndex, endIndex)

                    val matcher: Matcher = pattern.matcher(styleAttribute)
                    if (matcher.find()) {
                        return matcher.group(1)?.toFloat() ?: constTextSize
                    }
                }

                return constTextSize
            }

            private fun parseColorFromStyleAttribute(style: String): Int {
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
                val colorMatch = colorRegex.find(style)
                val colorCode =
                    colorMatch?.value?.substringAfter("color:")?.trimEnd(';')?.replace(" ", "")

                return try {
                    Color.parseColor(colorCode)
                } catch (e: IllegalArgumentException) {
                    Color.BLACK
                }
            }
        }
    }

    private class ResultValue(
        val value: String,
        val types: List<TypeValue>,
        val textSize: Float,
        val color: Int,
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

