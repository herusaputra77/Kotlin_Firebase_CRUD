package com.herusaputra.crudfirebaseapp

import android.os.Bundle
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

class AddMataKuliahActivity : AppCompatActivity() {

    private lateinit var edMatkul : EditText
    private lateinit var edSks : EditText
    private lateinit var btnSubmit : Button
    private lateinit var lvMatkul : ListView
    private lateinit var matKulList : MutableList<Matakuliah>
    private lateinit var database : DatabaseReference

    companion object{
        const val EXTRA_NAMA = "extra_nama"
        const val EXTRA_ID = "extra_id"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_mata_kuliah)
        edMatkul = findViewById(R.id.ed_matkul)
        edSks = findViewById(R.id.ed_sks)
        btnSubmit = findViewById(R.id.btn_submitMk)
        lvMatkul = findViewById(R.id.lv_mk)

        val id  = intent.getStringExtra(EXTRA_ID)
        val matkul = intent.getStringExtra(EXTRA_NAMA)
        matKulList = mutableListOf()
        database = FirebaseDatabase.getInstance("https://crudfirebaseapp-2024-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("mata_kuliah").child(id!!)

        btnSubmit.setOnClickListener{
            saveMatkul()
        }


    }
    fun saveMatkul(){
        val matkul = edMatkul.text.toString().trim()
        val sksText = edSks.text.toString().trim()
        val sks = sksText.toInt()

        if (matkul.isEmpty()){
            edMatkul.error = "Mata kuliah harus diisi"
            Toast.makeText(this@AddMataKuliahActivity,"Mata kuliah harus diisi", Toast.LENGTH_SHORT).show()

            return
        }

        if (sksText.isEmpty()){
            edSks.error = "Sks harus diisi"
            return
        }

        val matkulId = database.push().key
        val matakuliah = Matakuliah(matkulId!!, matkul , sks )

        if(matkulId != null){
            database.child(matkulId).setValue(matakuliah).addOnSuccessListener {
                Toast.makeText(applicationContext, "Data berhasil ditambah", Toast.LENGTH_SHORT).show()
            }
        }

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    matKulList.clear()
                    for (h in p0.children){
                        val matakuliah = h.getValue(Matakuliah::class.java)
                        if (matakuliah != null) {
                            matKulList.add(matakuliah)
                        }
                    }
                    val adapter = MatakuliahAdapter(this@AddMataKuliahActivity, R.layout.item_matkul, matKulList)
                    lvMatkul.adapter = adapter

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}