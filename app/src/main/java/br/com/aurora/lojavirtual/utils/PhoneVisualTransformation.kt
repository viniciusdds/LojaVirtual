package br.com.aurora.lojavirtual.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text.filter { it.isDigit() }
        val isCelular = originalText.length >= 3 && originalText[2] == '9'
        val maxDigits = if (isCelular) 11 else 10
        val trimmed = originalText.take(maxDigits)
        val out = StringBuilder()

        var i = 0
        while (i < trimmed.length) {
            when (i) {
                0 -> out.append("(" + trimmed[i])
                1 -> out.append(trimmed[i] + ") ")
                in 3..6 -> out.append(trimmed[i])
                6 -> {
                    if (isCelular) {
                        out.insert(out.length - 1, '-') // insere o hífen antes do caractere anterior
                        out.append(trimmed[i])
                    } else {
                        out.append("-").append(trimmed[i])
                    }
                }
                7 -> {
                    if (isCelular) {
                        out.append("-").append(trimmed[i])
                    } else {
                        out.insert(out.length - 1, '-') // insere o hífen antes do caractere anterior
                        out.append(trimmed[i])
                    }
                }
                else -> out.append(trimmed[i])
            }
            i++
        }

        val transformedText = out.toString()

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 0 -> offset
                    offset == 1 -> offset + 1 // + (
                    offset in 2..5 -> offset + 2 // + ) + espaço
                    offset in 6..10 -> offset + 3 // + -
                    else -> transformedText.length
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 1 -> 0
                    offset in 2..3 -> 1
                    offset in 4..6 -> offset - 2
                    offset in 7..11 -> offset - 3
                    offset >= 12 -> 11
                    else -> offset
                }.coerceIn(0, trimmed.length)
            }
        }

        return TransformedText(AnnotatedString(transformedText), offsetMapping)
    }
}
