package com.dis.ornetcalling.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.load
import com.dis.ornetcalling.R

class ImageAdapter(
    private val images: List<String>,
    private val onImageClick: (List<String>, Int) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val imageView = ImageView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
            adjustViewBounds = true
        }
        return ImageViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = images[position]
        holder.bind(imageUrl)
    }

    override fun getItemCount(): Int = minOf(images.size, 4)

    inner class ImageViewHolder(private val imageView: ImageView) :
        RecyclerView.ViewHolder(imageView) {
        init {
            imageView.setOnClickListener {
                onImageClick(images, adapterPosition)
            }
        }

        fun bind(imageUrl: String) {
            val size = when {
                images.size == 1 -> 200.dpToPx(imageView.context)
                images.size in 2..3 -> 100.dpToPx(imageView.context)
                else -> 80.dpToPx(imageView.context)
            }

            imageView.layoutParams = ViewGroup.MarginLayoutParams(size, size).apply {
                setMargins(4,4,4,4)
            }

            imageView.load(imageUrl) {
                placeholder(R.drawable.image_placeholder)
                error(R.drawable.image_error)
            }

            if (images.size > 4 && adapterPosition == 3) {
                // Add overlay for additional images
                val overlay = FrameLayout(imageView.context)
                overlay.layoutParams = ViewGroup.LayoutParams(size, size)
                overlay.setBackgroundColor(Color.parseColor("#80000000"))

                val textView = TextView(imageView.context)
                textView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                textView.text = "+${images.size - 3}"
                textView.setTextColor(Color.WHITE)
                textView.textSize = 16f

                overlay.addView(textView)
              //  overlay.gravity = Gravity.CENTER

                (imageView.parent as? ViewGroup)?.addView(overlay)
            }
        }
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}