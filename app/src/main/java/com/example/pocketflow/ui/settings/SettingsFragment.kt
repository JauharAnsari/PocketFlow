package com.example.pocketflow.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.pocketflow.R
import com.example.pocketflow.data.PreferenceManager
import com.example.pocketflow.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceManager: PreferenceManager

    private val avatars = arrayOf(
        R.drawable.chick_avtar,
        R.drawable.panda_avtar,
        R.drawable.penguin_avtar,
        R.drawable.turtle_avtar
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        preferenceManager = PreferenceManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupThemeToggle()
        setupCurrencySpinner()
        setupBiometricToggle()
        setupProfileCard()
    }

    override fun onResume() {
        super.onResume()
        updateProfileUI()
    }

    private fun setupProfileCard() {
        binding.profileCard.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileActivity::class.java))
        }
    }

    private fun updateProfileUI() {
        val userName = preferenceManager.profileName
        binding.profileNameText.text = userName
        
        // Generate dynamic email: name_pocketflow.app
        val email = "${userName.lowercase().replace(" ", "_")}_pocketflow.app"
        binding.profileEmailText.text = email

        binding.profileIcon.setImageResource(avatars[preferenceManager.profileAvatarIndex])
        binding.profileIcon.imageTintList = null // Remove tint if any to show avatar colors
    }

    private fun setupBiometricToggle() {
        binding.biometricToggle.isChecked = preferenceManager.isBiometricEnabled
        binding.biometricToggle.setOnCheckedChangeListener { _, isChecked ->
            preferenceManager.isBiometricEnabled = isChecked
        }
    }

    private fun setupThemeToggle() {
        val currentMode = AppCompatDelegate.getDefaultNightMode()
        binding.themeToggle.isChecked = currentMode == AppCompatDelegate.MODE_NIGHT_YES

        binding.themeToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setupCurrencySpinner() {
        val currencies = arrayOf("INR (₹)", "USD ($)", "EUR (€)", "GBP (£)")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.currencySpinner.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
