package com.example.booking_lab.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booking_lab.R
import com.example.booking_lab.adapter.JadwalAdapter
import com.example.booking_lab.adapter.LabAdapter
import com.example.booking_lab.adapter.RuanganLab
import com.example.booking_lab.databinding.FragmentDashboardBinding
import com.example.booking_lab.model.JadwalLab
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private var isShowingHistory = false

    // Data Statis Daftar Lab Fisik
    private val daftarLabStatis = listOf(
        RuanganLab("Lab. Komputer", "Gedung AH - Lantai 1 - Ruang AH.1.13"),
        RuanganLab("Lab. Antena", "Gedung Al - Ruang AH.6"),
        RuanganLab("Lab. Radio", "Gedung AH - Lantai 3 - Ruang AH.3.3")
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        if (userId.isNotEmpty()) {
            FirebaseDatabase.getInstance().getReference("users").child(userId)
                .get().addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        binding.tvGreeting.text = "Halo, ${snapshot.child("nama").value.toString()}!"
                    }
                }
            muatDaftarLab()
        }

        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_to_settingsFragment)
        }

        val scale = resources.displayMetrics.density

        binding.fabHistory.setOnClickListener {
            isShowingHistory = !isShowingHistory
            if (isShowingHistory) {
                binding.layoutHeader.visibility = View.GONE
                binding.tvSectionTitle.text = "Riwayat Peminjaman"
                binding.tvSectionTitle.textSize = 24f
                binding.tvSectionTitle.setPadding(0, (24 * scale).toInt(), 0, 0)
                binding.fabHistory.setImageResource(android.R.drawable.ic_menu_revert)
                muatRiwayatBooking(userId)
            } else {
                binding.layoutHeader.visibility = View.VISIBLE
                binding.tvSectionTitle.text = "Jadwal Laboratorium Tersedia"
                binding.tvSectionTitle.textSize = 16f
                binding.tvSectionTitle.setPadding(0, 0, 0, 0)
                binding.fabHistory.setImageResource(android.R.drawable.ic_menu_recent_history)
                muatDaftarLab()
            }
        }
    }

    // Fungsi menampilkan daftar Lab fisik yang bisa diklik
    private fun muatDaftarLab() {
        if (_binding != null && context != null) {
            binding.rvBeranda.layoutManager = LinearLayoutManager(requireContext())
            binding.rvBeranda.adapter = LabAdapter(daftarLabStatis) { labTerpilih ->
                val bundle = Bundle().apply {
                    putString("namaLab", labTerpilih.nama)
                    putString("lokasiLab", labTerpilih.lokasi)
                }
                findNavController().navigate(R.id.action_dashboardFragment_to_pilihJadwalFragment, bundle)
            }
        }
    }

    // Fungsi menampilkan riwayat reservasi mahasiswa
    private fun muatRiwayatBooking(userId: String) {
        FirebaseDatabase.getInstance().getReference("jadwal_lab")
            .orderByChild("dipesanOleh").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<JadwalLab>()
                    for (data in snapshot.children) {
                        val jadwal = data.getValue(JadwalLab::class.java)
                        if (jadwal != null) list.add(jadwal)
                    }
                    if (_binding != null && context != null) {
                        binding.rvBeranda.layoutManager = LinearLayoutManager(requireContext())
                        // Menambahkan parameter eksplisit 'jadwal ->' untuk mencegah amibiguitas lambda compiler
                        binding.rvBeranda.adapter = JadwalAdapter(list, "", true) { _ ->
                            Toast.makeText(requireContext(), "Lab ini sudah kamu reservasi.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}