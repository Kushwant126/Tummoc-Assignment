package com.example.tummocassignment.extensions

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.TypefaceSpan
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.tummocassignment.R

private var toast: Toast? = null
fun Context.showToastMessageNew(message: String) {
    toast?.cancel()
    toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast?.show()
}
fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.debounceClickListener(listener: View.OnClickListener) {
    val zoomIn = AnimationUtils.loadAnimation(context, R.anim.zoomin)
    val zoomOut = AnimationUtils.loadAnimation(context, R.anim.zoomout)

    this.setOnClickListener {
        // Start zoom-in animation
        hideKeyboard()
        this.startAnimation(zoomIn)

        // Listen for the end of the zoom-in animation
        zoomIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                this@debounceClickListener.startAnimation(zoomOut) // Start zoom-out animation after zoom-in ends

                // Set listener for zoom-out animation end
                zoomOut.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {}
                    override fun onAnimationRepeat(animation: Animation?) {}
                    override fun onAnimationEnd(animation: Animation?) {
                        listener.onClick(this@debounceClickListener) // Trigger click listener after both animations are done
                    }
                })
            }
        })
    }
}


@RequiresApi(Build.VERSION_CODES.P)
fun EditText.setCustomFonts(boldFont: Typeface, regularFont: Typeface) {
    // Set initial font based on whether there's text
    updateTypefaceOnInputChange(boldFont, regularFont)

    // Listen for text changes
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            updateTypefaceOnInputChange(boldFont, regularFont)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })

    // Apply regular font to the hint
    hint?.let {
        val spannable = SpannableString(it)
        spannable.setSpan(TypefaceSpan(regularFont), 0, it.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        hint = spannable
    }
}
fun disableEditText(editText: EditText) {
    editText.isFocusable = false
    editText.isFocusableInTouchMode = false
    editText.isClickable = false
    editText.isLongClickable = false
}

@RequiresApi(Build.VERSION_CODES.P)
private fun EditText.updateTypefaceOnInputChange(boldFont: Typeface, regularFont: Typeface) {
    this.typeface = if (this.text.isNullOrEmpty()) regularFont else boldFont
}

