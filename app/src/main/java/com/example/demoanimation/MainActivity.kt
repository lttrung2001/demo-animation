package com.example.demoanimation

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet.Motion
import androidx.core.app.ActivityCompat
import java.io.ByteArrayOutputStream
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var parentView: LinearLayout
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private val handler = Handler(Looper.getMainLooper())
    private var x1: Int = 0
    private var x2: Int = 0
    private var y1: Int = 0
    private var y2: Int = 0
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val anim = AnimationUtils.loadAnimation(this, R.anim.hide_item_anim)

        parentView = findViewById(R.id.parent)
        imageView = findViewById(R.id.img)
        textView = findViewById(R.id.textView)
        textView.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    x1 = motionEvent.x.toInt()
                    y1 = motionEvent.y.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    x2 = motionEvent.x.toInt()
                    y2 = motionEvent.y.toInt()
                    view.x += (x2 - x1).toFloat()
                    view.y += (y2 - y1).toFloat()
                }
            }
            return@setOnTouchListener true
        }
        imageView.setOnClickListener {
            try {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
                val bitmap = Bitmap.createBitmap(parentView.width, parentView.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                parentView.draw(canvas)
                val file = File("${externalCacheDir?.absolutePath}/hinh-anh-le-thanh-trung.jpeg")
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                file.writeBytes(stream.toByteArray())
                Toast.makeText(this, "Luu anh thanh cong!", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        parentView.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (motionEvent.x < 400F && view.x == 400F) {
                        view.animate()
                            .scaleY(1F)
                            .setDuration(450)
                            .rotationY(0F)
                            .translationX(view.x - 400F)
                            .start()
                        return@setOnTouchListener true
                    }
                    x1 = motionEvent.x.toInt()
                }
                MotionEvent.ACTION_UP -> {
                    x2 = motionEvent.x.toInt()
                }
            }
            val deltaX = x2 - x1
            if (deltaX > 200 && view.x == 0F) {
                view.animate()
                    .scaleY(0.8F)
                    .translationX(view.x + 400F)
                    .setDuration(450)
                    .rotationY(-30F)
                    .start()
            } else if (view.x == 400F) {
                view.animate()
                    .scaleY(1F)
                    .setDuration(450)
                    .rotationY(0F)
                    .translationX(view.x - 400F)
                    .start()
            }
            return@setOnTouchListener true
        }
    }
}