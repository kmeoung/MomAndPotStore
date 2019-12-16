package com.kmeoung.mapstore

import java.io.Serializable

data class BeanProduct(
    val idx: Int,
    val name: String,
    val price: Int,
    val count: Int,
    val img: String
) : Serializable