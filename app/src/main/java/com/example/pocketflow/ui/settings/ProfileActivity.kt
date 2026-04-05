package com.example.pocketflow.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pocketflow.R
import com.example.pocketflow.data.PreferenceManager
import com.example.pocketflow.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var preferenceManager: PreferenceManager
    private var selectedAvatarIndex = 0

    private val avatars = arrayOf(
        R.drawable.chick_avtar,
        R.drawable.panda_avtar,
        R.drawable.penguin_avtar,
        R.drawable.turtle_avtar
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)
        loadProfileData()
        setupListeners()
    }

    private fun loadProfileData() {
        binding.nameEditText.setText(preferenceManager.profileName)
        selectedAvatarIndex = preferenceManager.profileAvatarIndex
        updateAvatarPreview()
    }

    private fun setupListeners() {
        binding.avatar1.setOnClickListener { selectAvatar(0) }
        binding.avatar2.setOnClickListener { selectAvatar(1) }
        binding.avatar3.setOnClickListener { selectAvatar(2) }
        binding.avatar4.setOnClickListener { selectAvatar(3) }

        binding.saveButton.setOnClickListener {
            val newName = binding.nameEditText.text.toString().trim()
            if (newName.isNotEmpty()) {
                preferenceManager.profileName = newName
                preferenceManager.profileAvatarIndex = selectedAvatarIndex
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                binding.nameInputLayout.error = "Name cannot be empty"
            }
        }
    }

    private fun selectAvatar(index: Int) {
        selectedAvatarIndex = index
        updateAvatarPreview()
    }

    private fun updateAvatarPreview() {
        binding.profileImagePreview.setImageResource(avatars[selectedAvatarIndex])
    }
}
