package com.herusaputra.crudfirebaseapp

import android.content.Intent
import android.os.Bundle
import android.provider.Telephony.Mms.Intents
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private  lateinit var edtNama : EditText
    private  lateinit var edtAlamat : EditText
    private  lateinit var btnSubmit : Button
    private  lateinit var ref : DatabaseReference
    private  lateinit var mhsList : MutableList<Mahasiswa>
    private  lateinit var listMhs : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ref = FirebaseDatabase.getInstance("https://crudfirebaseapp-2024-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("mahasiswa")


        edtNama = findViewById(R.id.ed_nama)
        edtAlamat = findViewById(R.id.ed_alamat)
        btnSubmit = findViewById(R.id.btn_submit)
        listMhs = findViewById(R.id.lv_mhs)
        btnSubmit.setOnClickListener(this)
        mhsList = mutableListOf()

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    mhsList.clear()
                    for (h in p0.children){
                        val mahasiswa = h.getValue(Mahasiswa::class.java)
                        if (mahasiswa != null) {
                            mhsList.add(mahasiswa)
                        }
                    }
                    val adapter = MahasiswaAdapter(this@MainActivity, R.layout.item_mhs, mhsList)
                    listMhs.adapter = adapter

                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        listMhs.setOnItemClickListener { parent, view, position, id ->
            val mahasiswa = mhsList.get(position)
            val intent = Intent(this@MainActivity, AddMataKuliahActivity::class.java)

            intent.putExtra(AddMataKuliahActivity.EXTRA_ID, mahasiswa.id)
            intent.putExtra(AddMataKuliahActivity.EXTRA_NAMA, mahasiswa.nama)
            startActivity(intent)
        }



    }

    override fun onClick(v: View?) {
        saveData()
    }

    private  fun saveData(){
        val nama = edtNama.text.toString().trim()
        val alamat = edtAlamat.text.toString().trim()
//        Log.d(TAG, nama)

        if (nama.isEmpty()){
            edtNama.error = "Masukan nama!"
        }
        if(alamat.isEmpty()){
            edtAlamat.error = "Masukan alamat!"
        }

        val mhsId = ref.push().key
        val mhs = Mahasiswa(mhsId!!,nama,alamat)

        if (mhsId != null) {
            ref.child(mhsId).setValue(mhs).addOnCompleteListener{
                Toast.makeText(applicationContext, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}