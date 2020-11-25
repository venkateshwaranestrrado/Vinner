package com.estrrado.vinner.data.models.response

import com.estrrado.vinner.data.models.Address

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nithu on 07/11/19.
 */
class CartResponse {
    @SerializedName("status")
    var status:String?= null
    @SerializedName("data")
    var data = DataDetails()
    @SerializedName("message")
    var message:String?= null
}

class DataDetails{
    @SerializedName("cart")
    var cart= CartDetails()
    @SerializedName("items_total")
    var item_total:String?= ""
//    @SerializedName("cart_items")
//    var cart_items:ArrayList<CartItem>?=  null
    @SerializedName("address")
    @Expose
    var address: Address? = null



}
class CartDetails{
    @SerializedName("total_amount")
    var totalamount:String?= ""
    @SerializedName("grand_total")
    var grand_total:String?= ""
    @SerializedName("currency")
    var currency:String?= ""


}
//class CartItem{
//    @SerializedName("cart_item_id")
//    var cart_item_id:String?=""
//    @SerializedName("cart_id")
//    var cart_id:String?= ""
//    @SerializedName("product_id")
//    var product_id:String?= ""
//    @SerializedName("product_name")
//    var product_name:String?= ""
//    @SerializedName("product_price")
//    var product_price:String?= ""
//    @SerializedName("product_quantity")
//    var product_quantity:String?= ""
//    @SerializedName("product_total")
//    var product_total:String?= ""
//    @SerializedName("currency")
//    var currency:String?= ""
//    @SerializedName("category_name")
//    var category_name:String?= ""
//    @SerializedName("product_image")
//    var product_image:String?= ""
//    @SerializedName("delivery")
//    var delivery:String?= ""
//
//}