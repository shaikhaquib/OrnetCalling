package com.dis.ornetcalling.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dis.ornetcalling.databinding.ActivityImageViewerBinding

class ImageViewerActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_IMAGES = "extra_images"
        private const val EXTRA_POSITION = "extra_position"

        fun start(context: Context, images: List<String>, position: Int) {
            val intent = Intent(context, ImageViewerActivity::class.java).apply {
                putStringArrayListExtra(EXTRA_IMAGES, ArrayList(images))
                putExtra(EXTRA_POSITION, position)
            }
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityImageViewerBinding
    private lateinit var imageAdapter: ImagePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val images = intent.getStringArrayListExtra(EXTRA_IMAGES) ?: return
        val initialPosition = intent.getIntExtra(EXTRA_POSITION, 0)

        imageAdapter = ImagePagerAdapter(images)
        binding.viewPager.adapter = imageAdapter
        binding.viewPager.setCurrentItem(initialPosition, false)
    }

    private class ImagePagerAdapter(private val images: List<String>) :
        RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val imageView = ImageView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
            return ImageViewHolder(imageView)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            holder.imageView.load(images[position])
        }

        override fun getItemCount(): Int = images.size

        class ImageViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)
    }
}