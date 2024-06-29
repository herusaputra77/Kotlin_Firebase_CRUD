package com.herusaputra.crudfirebaseapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

data class MahasiswaAdapter( val mCtx : Context, val layoutResId: Int, val mhsList: List<Mahasiswa>) : ArrayAdapter<Mahasiswa>(mCtx, layoutResId,
    mhsList){

    private lateinit var database:DatabaseReference
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)

        val view : View = layoutInflater.inflate(layoutResId, null)
        val tvNama : TextView = view.findViewById(R.id.tv_nama)
        val tvAlamat : TextView = view.findViewById(R.id.tv_alamat)
        val tvEdit : TextView = view.findViewById(R.id.tv_edit)
        val tvHapus : TextView = view.findViewById(R.id.tv_hapus)


        val mahahasiswa : Mahasiswa = mhsList[position]

        tvEdit.setOnClickListener{
            showUpdateDialog(mahahasiswa)
        }
        tvHapus.setOnClickListener{
            hapusData(mahahasiswa)
        }
        tvNama.text = mahahasiswa.nama
        tvAlamat.text = mahahasiswa.alamat

        return  view
    }
    fun hapusData(mahasiswa: Mahasiswa){
        database = FirebaseDatabase.getInstance("https://crudfirebaseapp-2024-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("mahasiswa")
        database.child(mahasiswa.id).removeValue().addOnCompleteListener{
            Toast.makeText(mCtx,"Data berhasil di Hapus", Toast.LENGTH_SHORT).show()
        }
    }

    fun showUpdateDialog(mahasiswa: Mahasiswa){

        val builder = AlertDialog.Builder(mCtx)
        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.update_dialog, null)
        val etNama = view.findViewById<EditText>(R.id.ed_nama)
        val etAlamat = view.findViewById<EditText>(R.id.ed_alamat)

        etNama.setText(mahasiswa.nama)
        etAlamat.setText(mahasiswa.alamat)


//        Log.d(TAG, nama)

//
        builder.setView(view)
        builder.setTitle("Edit Data ${mahasiswa.nama}")
        builder.setPositiveButton("Update"){p0,p1 ->
            val dbMhs = FirebaseDatabase.getInstance("https://crudfirebaseapp-2024-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("mahasiswa")
            val nama = etNama.text.toString().trim()
            val alamat = etAlamat.text.toString().trim()
//            val mhs = mapOf<String, String>(
//                "nama" to nama,
//                "alamat" to alamat
//            )
            if (nama.isEmpty()){
                etNama.error = "Masukan nama!"
                etNama.requestFocus()
                return@setPositiveButton
            }
            if(alamat.isEmpty()){
                etAlamat.error = "Masukan alamat!"
                etAlamat.requestFocus()
                return@setPositiveButton
            }
            val mhs = Mahasiswa(mahasiswa.id, nama, alamat)

            dbMhs.child(mahasiswa.id).setValue(mhs).addOnCompleteListener{

                Toast.makeText(mCtx, "Data berhasil di Update", Toast.LENGTH_SHORT).show()
            }

//            }.addOnFailureListener{
//                Toast.makeText(mCtx,"Failed to Update",Toast.LENGTH_SHORT).show()
//            }
        }
        builder.setNegativeButton("No"){p0,p1 ->

        }

        val alert = builder.create()
        alert.show()
    }
}