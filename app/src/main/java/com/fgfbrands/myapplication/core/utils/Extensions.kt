package com.fgfbrands.myapplication.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Provides extension functions for common operations.
 *
 * Key Highlights:
 * - Simplifies date formatting with an extension function.
 */
fun Date.formatTo(pattern: String): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(this)
}
