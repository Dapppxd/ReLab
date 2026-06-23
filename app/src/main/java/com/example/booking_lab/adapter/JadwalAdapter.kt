package com.example.booking_lab.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.booking_lab.databinding.ItemJadwalBinding
import com.example.booking_lab.model.JadwalLab

class JadwalAdapter(
    private val listJadwal: List<JadwalLab>,
    private val lokasiPasti: String = "", // Default kosong
    private val isHistory: Boolean = false, // Parameter baru untuk deteksi mode riwayat
    private val onItemClick: (JadwalLab) -> Unit
) : RecyclerView.Adapter<JadwalAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemJadwalBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemJadwalBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jadwal = listJadwal[position]
        holder.binding.tvJadwalNamaLab.text = jadwal.namaLab

        // Logika Lokasi (Statis jika di halaman Pilih Jadwal, Dinamis jika di halaman Riwayat)
        val lokasi = if (lokasiPasti.isNotEmpty()) lokasiPasti else {
            when (jadwal.namaLab) {
                "Lab. Komputer" -> "Gedung AH - Lantai 1 - Ruang AH.1.13"
                "Lab. Antena" -> "Gedung Al - Ruang AH.6"
                "Lab. Radio" -> "Gedung AH - Lantai 3 - Ruang AH.3.3"
                else -> "Lokasi Tidak Diketahui"
            }
        }
        holder.binding.tvJadwalLokasi.text = lokasi
        holder.binding.tvJadwalWaktu.text = "${jadwal.tanggal} | ${jadwal.waktu}"

        // Logika Warna Status Badge
        if (isHistory) {
            // Jika masuk mode Riwayat, paksa teks menjadi Dipesan dan berwarna biru terang
            holder.binding.tvJadwalStatus.text = "Dipesan"
            holder.binding.tvJadwalStatus.setTextColor(Color.parseColor("#1565C0"))
            holder.itemView.alpha = 1.0f
            holder.itemView.setOnClickListener { onItemClick(jadwal) }
        } else {
            // Jika masuk mode Pilih Jadwal, filter berdasarkan ketersediaan
            if (jadwal.status == "Tersedia") {
                holder.binding.tvJadwalStatus.text = "Tersedia"
                holder.binding.tvJadwalStatus.setTextColor(Color.parseColor("#1565C0"))
                holder.itemView.alpha = 1.0f
                holder.itemView.setOnClickListener { onItemClick(jadwal) }
            } else {
                holder.binding.tvJadwalStatus.text = "Tidak Tersedia"
                holder.binding.tvJadwalStatus.setTextColor(Color.GRAY)
                holder.itemView.alpha = 0.7f
                holder.itemView.setOnClickListener(null)
            }
        }
    }

    override fun getItemCount() = listJadwal.size
}