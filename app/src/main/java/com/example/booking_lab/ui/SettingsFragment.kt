package com.example.booking_lab.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.booking_lab.R
import com.example.booking_lab.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val userId = user?.uid ?: return
        val dbRef = FirebaseDatabase.getInstance().getReference("users").child(userId)

        // 1. Tampilkan Data Saat Ini
        binding.etEditEmail.setText(user.email)
        dbRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                binding.etEditNama.setText(snapshot.child("nama").value.toString())
            }
        }

        // 2. Simpan Perubahan (Nama & Email)
        binding.btnSimpanProfil.setOnClickListener {
            val namaBaru = binding.etEditNama.text.toString().trim()
            val emailBaru = binding.etEditEmail.text.toString().trim()

            if (namaBaru.isEmpty() || emailBaru.isEmpty()) {
                Toast.makeText(requireContext(), "Nama dan Email tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Update Nama di Realtime Database
            dbRef.child("nama").setValue(namaBaru)

            // Update Email di Firebase Auth
            if (emailBaru != user.email) {
                user.updateEmail(emailBaru).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        dbRef.child("email").setValue(emailBaru) // Sinkronisasi ke database
                        Toast.makeText(requireContext(), "Profil dan Email berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Gagal ubah email. Silakan login ulang lalu coba lagi.", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
            }
        }

        // 3. Reset Password
        binding.btnResetPassword.setOnClickListener {
            val email = user.email.toString()
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Link reset password dikirim ke $email", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Gagal mengirim link reset password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 4. Logout
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}