package com.herusaputra.crudfirebaseapp

data class Mahasiswa(
    val id : String,
    val nama : String,
    val alamat : String
){
    constructor(): this("","","")
}