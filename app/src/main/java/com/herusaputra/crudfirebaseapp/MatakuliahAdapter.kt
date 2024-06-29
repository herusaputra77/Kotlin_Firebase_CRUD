package com.herusaputra.crudfirebaseapp

import android.content.Context
import android.content.LocusId
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

data class MatakuliahAdapter(val mCtx : Context, val layoutResId : Int, val matkulList: List<Matakuliah>) : ArrayAdapter<Matakuliah>(mCtx, layoutResId,
    matkulList){

//    private lateinit var database : DatabaseReference
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)
        val view : View = layoutInflater.inflate(layoutResId, null)
        val tvMatkul  = view.findViewById<TextView>(R.id.tv_matkul)
        val tvSks  = view.findViewById<TextView>(R.id.tv_sks)

        val matkul : Matakuliah = matkulList[position]
        tvMatkul.text = matkul.nm_mk
        tvSks.text = matkul.sks.toString()
        return view

    }

}