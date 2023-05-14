package com.example.parsehtml

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.Log

class GenHtmlCompat {

    companion object{
        fun fromHtml(htmlText: String): Spanned {
            val getListSpan = getListSpan(value = htmlText)


            var result = ""

            getListSpan.forEach {
                val value: ParserHtmlValue.ResultValue = ParserHtmlValue.getValue(it)
                result += value.toString()
            }

            Log.d("GenHtmlCompat", result)
            return  SpannableStringBuilder(result)
        }



        private  fun getListSpan(value: String): List<String>{
            val regex = Regex("<span[^>]*>.*?</span>")
            val spanMatches = regex.findAll(value)

            val spanList = mutableListOf<String>()

            for (match in spanMatches) {
                spanList.add(match.value)
            }

            return  spanList
        }

    }

    private class ParserHtmlValue{
        companion object{
            private val bold = Pair("<strong>", "</strong>")
            private val italic = Pair("<em>", "</em>")
            private val underline = Pair("<ins>", "</ins>")

            fun getValue(htmlText: String): ResultValue{
                val listTypes = mutableListOf<TypeValue>()
                var value: String = parseValue(htmlText)
                if(value.contains(bold.first)){
                    value = value.replace(bold.first, "").replace(bold.second, "")
                    listTypes.add(TypeValue.BOLD)
                }
                if(value.contains(italic.first)){
                    value = value.replace(italic.first, "").replace(italic.second, "")
                    listTypes.add(TypeValue.ITALIC)
                }
                if(value.contains(underline.first)){
                    value = value.replace(underline.first, "").replace(underline.second, "")
                    listTypes.add(TypeValue.UNDERLINE)
                }

                return ResultValue(value, listTypes)
            }

            private fun parseValue(htmlText: String): String{
                val startTextToParse = ">"
                val endTextToParse = "</span>"
                val startIndex = htmlText.indexOf(startTextToParse) + 1
                val endIndex = htmlText.indexOf(endTextToParse)
                return  htmlText.substring(startIndex, endIndex)
            }


        }

        class ResultValue(
            private val value: String,
            private val types: List<TypeValue>
        ){
            override fun toString(): String {
                return "ResultValue(value='$value', types=$types)"
            }
        }

        enum class TypeValue{
            BOLD, UNDERLINE, ITALIC
        }
    }

}

