package com.empty.heartmonitor.tracking.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.empty.heartmonitor.tracking.presentation.ui.TrackingScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrackingFragment : Fragment() {

    private val viewModel: TrackingViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                TrackingScreen(viewModel)
            }
        }
    }

}