package com.herusaputra.crudfirebaseapp

data class Matakuliah(
    val id : String,
    val nm_mk: String,
    val sks : Int
){
    constructor():this("","",0)
}