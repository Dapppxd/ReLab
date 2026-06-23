package com.example.booking_lab.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booking_lab.R
import com.example.booking_lab.adapter.LabAdapter
import com.example.booking_lab.adapter.RuanganLab
import com.example.booking_lab.databinding.FragmentAdminDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AdminDashboardFragment : Fragment() {

    private var _binding: FragmentAdminDashboardBinding? = null
    private val binding get() = _binding!!

    // Data Statis Daftar Lab Fisik (Sama dengan Mahasiswa)
    private val daftarLabStatis = listOf(
        RuanganLab("Lab. Komputer", "Gedung AH - Lantai 1 - Ruang AH.1.13"),
        RuanganLab("Lab. Antena", "Gedung Al - Ruang AH.6"),
        RuanganLab("Lab. Radio", "Gedung AH - Lantai 3 - Ruang AH.3.3")
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        if (userId.isNotEmpty()) {
            // Ambil Nama Admin dari Database
            FirebaseDatabase.getInstance().getReference("users").child(userId)
                .get().addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        binding.tvGreetingAdmin.text = "Halo, ${snapshot.child("nama").value.toString()}!"
                    }
                }

            // Panggil fungsi untuk menampilkan daftar lab
            muatDaftarLab()
        }

        // Tombol Logout
        binding.btnSettingsAdmin.setOnClickListener {
            findNavController().navigate(R.id.action_to_settingsFragment)
        }

        // Tombol Tambah Jadwal Lab
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_adminDashboardFragment_to_addLabFragment)
        }
    }

    // Fungsi menampilkan daftar Lab fisik yang bisa diklik
    private fun muatDaftarLab() {
        if (_binding != null && context != null) {
            binding.rvBerandaAdmin.layoutManager = LinearLayoutManager(requireContext())
            binding.rvBerandaAdmin.adapter = LabAdapter(daftarLabStatis) { labTerpilih ->
                // Admin juga bisa mengklik lab untuk melihat isi jadwal di dalamnya
                val bundle = Bundle().apply {
                    putString("namaLab", labTerpilih.nama)
                    putString("lokasiLab", labTerpilih.lokasi)
                }
                findNavController().navigate(R.id.action_adminDashboardFragment_to_pilihJadwalFragment, bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}