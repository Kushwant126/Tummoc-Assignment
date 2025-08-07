package com.example.tummocassignment.bindingAdapters

import android.content.res.ColorStateList
import android.content.res.Resources
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView
import org.jetbrains.annotations.NotNull

object  DataBindingAdaptersFile {
    @JvmStatic
    @BindingAdapter(value = ["bind:isVisible"], requireAll = false)
    fun setVisibility(view: View, isVisible: Boolean?) {
//        view.visibility = if (isVisible == true) View.VISIBLE else View.GONE
//        view.isVisible = isVisible.isTrue()
        view.isVisible = isVisible == true // default is false
    }

    @JvmStatic
    @BindingAdapter(value = ["bind:bindbackgroundColor"], requireAll = false)
    fun setBackgroundColor(view: View, color: Int?) {
//        color?.let { view.setBackgroundColor(it) }
        color?.let { view.setBackgroundColor(ContextCompat.getColor(view.context, it)) }
    }

    @JvmStatic
    @BindingAdapter(value = ["bind:bindFontFamily"], requireAll = false)
    fun setFontFamily(textView: TextView, fontRes: Int?) {
        fontRes?.let { ResourcesCompat.getFont(textView.context, it)?.let { tf -> textView.typeface = tf } }
    }
    @JvmStatic
    @BindingAdapter(value = ["bind:bindTextColor"], requireAll = false)
    fun setTextColor(view: TextView, colorResId: Int) {
        view.setTextColor(ContextCompat.getColor(view.context, colorResId))
    }

}

object ImageBindingAdapter {
    @JvmStatic
    @BindingAdapter(value = ["bind:bindBackgroundDrawable", "bind:bindSrcDrawable", "bind:bindImageTintColor"], requireAll = false)
    fun bindDrawables(@NotNull imageView: ImageView, backgroundResId: Int?, srcResId: Int?, @ColorRes tintColor: Int? = null) {
        if (backgroundResId != null) {
            imageView.background = ContextCompat.getDrawable(imageView.context, backgroundResId)
        } else {
            imageView.background = null
        }
        if (srcResId != null) {
            imageView.setImageResource(srcResId)
        } else {
            imageView.setImageDrawable(null)
        }

        if (tintColor!= null) {
            imageView.imageTintList = ContextCompat.getColorStateList(imageView.context,tintColor)
        }
    }
    @JvmStatic
    @BindingAdapter(value = ["bind:bindBackgroundDrawable", "bind:bindSrcDrawable", "bind:bindImageTintColor"], requireAll = false)
    fun bindDrawablesNewOld(imageView: ImageView, backgroundResId: Int?, srcResId: Int?, @ColorRes tintColor: Int?) {
        backgroundResId?.let {
            try {
                val drawable = ContextCompat.getDrawable(imageView.context, it)
                if (drawable != null) {
                    imageView.background = drawable
                } else {
                    imageView.background = null
                }
            } catch (e: Resources.NotFoundException) {
                imageView.background = null
                Log.e("ImageBindingAdapter", "Invalid background resource: $it", e)
            }
        }

        srcResId?.let {
            try {
                val drawable = ContextCompat.getDrawable(imageView.context, it)
                if (drawable != null) {
                    imageView.setImageDrawable(drawable)
                } else {
                    imageView.setImageDrawable(null)
                }
            } catch (e: Resources.NotFoundException) {
                imageView.setImageDrawable(null)
                Log.e("ImageBindingAdapter", "Invalid src resource: $it", e)
            }
        }

        tintColor?.let {
            imageView.imageTintList = ContextCompat.getColorStateList(imageView.context, it)
        } ?: run {
            imageView.imageTintList = null
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["bind:bindSrcDrawable"], requireAll = false)
    fun bindDrawablesNew(imageView: ImageView, srcResId: Int?) {
        // Set src drawable
        srcResId?.let {
            try {
                val drawable = ContextCompat.getDrawable(imageView.context, it)
                imageView.setImageDrawable(drawable)
            } catch (e: Resources.NotFoundException) {
                imageView.setImageDrawable(null)
                Log.e("ImageBindingAdapter", "Invalid src resource: $it", e)
            }
        }
    }
}

object CardViewBindingAdapter {
    @JvmStatic
    @BindingAdapter(value = ["bind:bindCardElevation", "bind:bindCardBackgroundColor", "bind:bindCardStrokeColor", "bind:bindCardStrokeWidth", ], requireAll = false)
    fun bindCardAttributes(cardView: MaterialCardView, elevation: Int?, @ColorRes backgroundColor: Int?, @ColorRes strokeColor: Int?, strokeWidth: Int? = 2) {

        elevation?.let { cardView.cardElevation = it.toFloat() }
        backgroundColor?.let { cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, it)) }
        strokeColor?.let { cardView.setStrokeColor(ContextCompat.getColorStateList(cardView.context, it)) }
        strokeWidth?.let { cardView.strokeWidth = it }
    }
}

object RadioButtonBindingAdapter {

    @JvmStatic
    @BindingAdapter("radioTintColor")
    fun setRadioButtonTint(radioButton: RadioButton, @ColorRes tintColorRes: Int?) {
        tintColorRes?.let {
            val color = ContextCompat.getColor(radioButton.context, it)
            radioButton.buttonTintList = ColorStateList.valueOf(color)
        }
    }

}

