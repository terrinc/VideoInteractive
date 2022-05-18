package com.example.videointeractive.presentation.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.videointeractive.R
import com.example.videointeractive.app.InteractiveApp
import com.example.videointeractive.databinding.ActivityInteractiveBinding
import com.example.videointeractive.presentation.viewmodel.InteractiveViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

private const val PROGRESS_MAX = 100
private const val PROGRESS_FREQUENCY_UPDATE = 100L //in ms

class InteractiveActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityInteractiveBinding::bind)

    @Inject
    lateinit var viewModel: InteractiveViewModel
    private lateinit var runnable: Runnable
    private var handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as InteractiveApp).appComponent.inject(this)
        setContentView(R.layout.activity_interactive)

        setListeners()
        setObservers()

    }

    private fun setListeners() {
        with(binding) {
            videoView.setOnPreparedListener { player ->
                getProgress()
                viewModel.onPreparedVideo()
            }
            videoView.setOnCompletionListener {
                viewModel.onCompleteVideo()
            }
            interactiveButton.setOnClickListener {
                viewModel.onHandClicked()
            }
        }
    }

    private fun setObservers() {
        with(viewModel) {
            observeCurrentVideo(this@InteractiveActivity, ::playVideo)
            observeCurrentProgress(this@InteractiveActivity, ::showProgress)
            observeProgressThresholdReached(this@InteractiveActivity, ::showInteractiveButton)
            observeMessageText(this@InteractiveActivity, ::showMessage)
        }
    }

    private fun getProgress() {
        runnable = Runnable {
            val position = binding.videoView.currentPosition
            val duration = binding.videoView.duration
            val currentPercent = position * PROGRESS_MAX / duration
            viewModel.setCurrentProgress(currentPercent)
            if (currentPercent != PROGRESS_MAX) {
                handler.postDelayed(runnable, PROGRESS_FREQUENCY_UPDATE)
            }
        }
        handler.postDelayed(runnable, PROGRESS_FREQUENCY_UPDATE)
    }

    private fun playVideo(uri: Uri) {
        with(binding.videoView) {
            setVideoURI(uri)
            start()
        }
    }

    private fun showProgress(progress: Int) {
        with(binding) {
            rightProgress.progress = progress
            leftProgress.progress = progress
        }
    }

    private fun showInteractiveButton(show: Boolean) {
        binding.interactiveButton.isVisible = show
    }

    private fun showMessage(text: String) {
        Snackbar.make(binding.interactiveButton, text, Snackbar.LENGTH_SHORT)
            .show()
    }
}
