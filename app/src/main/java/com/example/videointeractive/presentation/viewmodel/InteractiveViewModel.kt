package com.example.videointeractive.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.videointeractive.R
import com.example.videointeractive.presentation.helpers.ResourcesProvider
import javax.inject.Inject

class InteractiveViewModel @Inject constructor(private val resourcesProvider: ResourcesProvider) : ViewModel() {

    private val videoUriList = mutableListOf<Uri>()
    private val mutableCurrentVideo = MutableLiveData<Uri>()
    private val mutableCurrentProgress = MutableLiveData<Int>()
    private val mutableProgressThresholdReached = MutableLiveData<Boolean>()
    private val mutableMessageText = MutableLiveData<String>()
    private var currentVideoIndex = -1
    private var isVideoCompleted = false

    init {
        videoUriList.addAll(resourcesProvider.getVideosUriList())
        if (videoUriList.isNotEmpty()) {
            setNextVideo()
        }
    }

    fun observeCurrentVideo(
        livecycle: LifecycleOwner,
        observer: Observer<Uri>,
    ) {
        mutableCurrentVideo.observe(livecycle, observer)
    }

    fun observeCurrentProgress(
        livecycle: LifecycleOwner,
        observer: Observer<Int>,
    ) {
        mutableCurrentProgress.observe(livecycle, observer)
    }

    fun observeProgressThresholdReached(
        livecycle: LifecycleOwner,
        observer: Observer<Boolean>,
    ) {
        mutableProgressThresholdReached.observe(livecycle, observer)
    }

    fun observeMessageText(
        livecycle: LifecycleOwner,
        observer: Observer<String>,
    ) {
        mutableMessageText.observe(livecycle, observer)
    }

    fun setCurrentProgress(progress: Int) {
        mutableCurrentProgress.value = progress
        if (progress >= PROGRESS_THRESHOLD_PERCENT && mutableProgressThresholdReached.value != true) {
            mutableProgressThresholdReached.value = true
        }
    }

    fun onHandClicked() {
        if (currentVideoIndex < videoUriList.size - 1) {
            setNextVideo()
        } else {
            mutableMessageText.value = resourcesProvider.getStringResources(R.string.interactive_complete)
        }
    }

    fun onCompleteVideo() {
        isVideoCompleted = true
        mutableMessageText.value = resourcesProvider.getStringResources(R.string.restart_interactive)
    }

    fun onPreparedVideo() {
        mutableProgressThresholdReached.value = false
        isVideoCompleted = false
    }

    private fun setNextVideo() {
        if (isVideoCompleted) {
            currentVideoIndex = 0
        } else {
            currentVideoIndex++
        }
        mutableCurrentVideo.value = videoUriList[currentVideoIndex]
    }

    companion object {
        private const val PROGRESS_THRESHOLD_PERCENT = 30
    }
}
