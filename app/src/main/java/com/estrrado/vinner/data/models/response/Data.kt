package com.estrrado.vinner.data.models.response

import com.bluelinelabs.logansquare.annotation.JsonField
import com.estrrado.vinner.data.LatestProductItem
import com.google.gson.annotations.SerializedName

data class Data (

   /* @field:SerializedName("products")
    @field:JsonField(name = arrayOf("products"))
    val products: ArrayList<LatestProductItem?>? = null*/


    val results: List<LatestProductItem>


    )
