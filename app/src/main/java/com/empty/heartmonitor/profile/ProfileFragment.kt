package com.empty.heartmonitor.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.empty.heartmonitor.databinding.FragmentProfileBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModel()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.name.onEach {
            binding.textName.setText(it)
        }.launchIn(lifecycleScope)
        viewModel.token.onEach {
            binding.textToken.setText(it)
        }.launchIn(lifecycleScope)
        binding.saveBtn.setOnClickListener {
            viewModel.saveName(binding.textName.text.toString())
        }
        binding.textTokenLayout.setEndIconOnClickListener {
            shareToken(binding.textToken.text.toString())
        }
    }

    private fun shareToken(token: String) {
        val intent = Intent(Intent.ACTION_SEND)
        val shareBody = token
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_SUBJECT,
            token
        )
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(intent, "Відправити токен"))
    }
}