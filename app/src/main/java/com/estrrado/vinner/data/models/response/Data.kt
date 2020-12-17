package com.estrrado.vinner.data.models.response

import com.bluelinelabs.logansquare.annotation.JsonField
import com.estrrado.vinner.data.models.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Data {
    @SerializedName("logo")
    @Expose
    var logo: String? = null

    @SerializedName("banner_slider")
    @Expose
    var bannerSlider: List<BannerSlider>? = null

    @SerializedName("featured")
    @Expose
    var featured: List<Featured>? = null

    @SerializedName("categories")
    @Expose
    var categories: List<Category>? = null

    @SerializedName("regions")
    @Expose
    var regions: List<Region>? = null

    @SerializedName("access_token")
    @Expose
    private var accessToken: String? = null

    @SerializedName("username")
    @Expose
    private var username: String? = null

    @SerializedName("review_id")
    @Expose
    private var review_id: String? = null

//    @SerializedName("email")
//    @Expose
//    private var email: String? = null
//
//    @SerializedName("mobile")
//    @Expose
//    private var mobile: String? = null

    @SerializedName("cartcount")
    @Expose
    var cartcount: String? = null

    @SerializedName("cartid")
    @Expose
    public var cartid: String? = null

    @SerializedName("redirect")
    @Expose
    private var redirect: String? = null

    fun getAccessToken(): String? {
        return accessToken
    }

    fun setAccessToken(accessToken: String?) {
        this.accessToken = accessToken
    }

    fun getUsername(): String? {
        return username
    }

    fun setUsername(username: String?) {
        this.username = username
    }

//    fun getEmail(): String? {
//        return email
//    }
//
//    fun setEmail(email: String?) {
//        this.email = email
//    }
//
//    fun getMobile(): String? {
//        return mobile
//    }
//
//    fun setMobile(mobile: String?) {
//        this.mobile = mobile
//    }

    fun getRedirect(): String? {
        return redirect
    }

    fun setRedirect(redirect: String?) {
        this.redirect = redirect
    }

    @SerializedName("product")
    @Expose
    private var product: Product? = null

    @SerializedName("reviews")
    @Expose
    private var reviews: List<Review?>? = null



    @SerializedName("related_products")
    @Expose
    private var related_products: ArrayList<RelatedProducts>? = null

    fun getProduct(): Product? {
        return product
    }

    fun setProduct(product: Product?) {
        this.product = product
    }

    fun getReviews(): List<Review?>? {
        return reviews
    }

    fun setReviews(reviews: List<Review?>?) {
        this.reviews = reviews
    }

    fun getRelatedProducts(): ArrayList<RelatedProducts>? {
        return related_products
    }

    fun setRelatedProducts(relatedProducts: ArrayList<RelatedProducts>? ) {
        this.related_products = relatedProducts
    }

    /*
    cart page
     */

    @SerializedName("cart")
    @Expose
    private var cart: Cart? = null

    @SerializedName("items_total")
    @Expose
    private var itemsTotal: String? = null

    @SerializedName("cart_items")
    @Expose
    private var cartItems: ArrayList<CartItem?>? = null

    fun getCart(): Cart? {
        return cart
    }

    fun setCart(cart: Cart?) {
        this.cart = cart
    }

    fun getItemsTotal(): String? {
        return itemsTotal
    }

    fun setItemsTotal(itemsTotal: String?) {
        this.itemsTotal = itemsTotal
    }

    fun getCartItems(): ArrayList<CartItem?>? {
        return cartItems
    }

    fun setCartItems(cartItems: ArrayList<CartItem?>?) {
        this.cartItems = cartItems
    }

    /*
    Add cart
     */
    @SerializedName("cart_id")
    @Expose
    private var cartId: Int? = null

    @SerializedName("vendor_id")
    @Expose
    private var vendorId: String? = null

    @SerializedName("ptoduct_id")
    @Expose
    private var ptoductId: String? = null

    @SerializedName("product_name")
    @Expose
    private var productName: String? = null

    @SerializedName("product_qty")
    @Expose
    private var productQty: String? = null

    @SerializedName("product_price")
    @Expose
    private var productPrice: String? = null

    @SerializedName("product_total")
    @Expose
    private var productTotal: String? = null

    @SerializedName("total_amount")
    @Expose
    private var totalAmount: String? = null

    @SerializedName("grand_total")
    @Expose
    private var grandTotal: String? = null

    fun getCartId(): Int? {
        return cartId
    }

    fun setCartId(cartId: Int?) {
        this.cartId = cartId
    }

    fun getVendorId(): String? {
        return vendorId
    }

    fun setVendorId(vendorId: String?) {
        this.vendorId = vendorId
    }

    fun getPtoductId(): String? {
        return ptoductId
    }

    fun setPtoductId(ptoductId: String?) {
        this.ptoductId = ptoductId
    }

    fun getProductName(): String? {
        return productName
    }

    fun setProductName(productName: String?) {
        this.productName = productName
    }

    fun getProductQty(): String? {
        return productQty
    }

    fun setProductQty(productQty: String?) {
        this.productQty = productQty
    }

    fun getProductPrice(): String? {
        return productPrice
    }

    fun setProductPrice(productPrice: String?) {
        this.productPrice = productPrice
    }

    fun getProductTotal(): String? {
        return productTotal
    }

    fun setProductTotal(productTotal: String?) {
        this.productTotal = productTotal
    }

    fun getTotalAmount(): String? {
        return totalAmount
    }

    fun setTotalAmount(totalAmount: String?) {
        this.totalAmount = totalAmount
    }

    fun getGrandTotal(): String? {
        return grandTotal
    }

    fun setGrandTotal(grandTotal: String?) {
        this.grandTotal = grandTotal
    }

    /*
    delivery fee
     */
    @SerializedName("total_weight")
    @Expose
    private var totalWeight: Int? = null

    @SerializedName("price")
    @Expose
    private var price: String? = null

    @SerializedName("delivery_fee")
    @Expose
    private var deliveryFee: String? = null

    @SerializedName("sub_total")
    @Expose
    private var subTotal: String? = null

    @SerializedName("currency")
    @Expose
    private var currency: String? = null

    @SerializedName("delivery_exp_date")
    @Expose
    private var deliveryExpDate: String? = null

    fun getTotalWeight(): Int? {
        return totalWeight
    }

    fun setTotalWeight(totalWeight: Int?) {
        this.totalWeight = totalWeight
    }

    fun getPrice(): String? {
        return price
    }

    fun setPrice(price: String?) {
        this.price = price
    }

    fun getDeliveryFee(): String? {
        return deliveryFee
    }

    fun setDeliveryFee(deliveryFee: String?) {
        this.deliveryFee = deliveryFee
    }

    fun getSubTotal(): String? {
        return subTotal
    }

    fun setSubTotal(subTotal: String?) {
        this.subTotal = subTotal
    }

    fun getCurrency(): String? {
        return currency
    }

    fun setCurrency(currency: String?) {
        this.currency = currency
    }

    fun getDeliveryExpDate(): String? {
        return deliveryExpDate
    }

    fun setDeliveryExpDate(deliveryExpDate: String?) {
        this.deliveryExpDate = deliveryExpDate
    }

    /*
    shipping operators
     */
    @SerializedName("shipping_operator_id")
    @Expose
    private var shippingOperatorId: String? = null

    @SerializedName("operator")
    @Expose
    private var operator: String? = null

    fun getShippingOperatorId(): String? {
        return shippingOperatorId
    }

    fun setShippingOperatorId(shippingOperatorId: String?) {
        this.shippingOperatorId = shippingOperatorId
    }

    fun getOperator(): String? {
        return operator
    }

    fun setOperator(operator: String?) {
        this.operator = operator
    }

    /*
    address
     */

    @SerializedName("address")
    @Expose
    private var address: Address? = null

    fun getAddress(): Address? {
        return address
    }

    fun setAddress(address: Address) {
        this.address = address
    }

    @field:SerializedName("user_id")
    @field:JsonField(name = arrayOf("user_id"))
    val user_id: String? = null

    @field:SerializedName("current_stock")
    @field:JsonField(name = arrayOf("current_stock"))
    val current_stock: String? = null

    @field:SerializedName("name")
    @field:JsonField(name = arrayOf("name"))
    val name: String? = null

    @field:SerializedName("address1")
    @field:JsonField(name = arrayOf("address1"))
    val address1: String? = null


    @field:SerializedName("address2")
    @field:JsonField(name = arrayOf("address2"))
    val address2: String? = null

    @field:SerializedName("post")
    @field:JsonField(name = arrayOf("post"))
    val post: String? = null

    @field:SerializedName("city")
    @field:JsonField(name = arrayOf("city"))
    val city: String? = null

    @field:SerializedName("state")
    @field:JsonField(name = arrayOf("state"))
    val state: String? = null

    @field:SerializedName("mobile")
    @field:JsonField(name = arrayOf("mobile"))
    val mobile: String? = null

    @field:SerializedName("country_id")
    @field:JsonField(name = arrayOf("country_id"))
    val country_id: String? = null

    @field:SerializedName("country_code")
    @field:JsonField(name = arrayOf("country_code"))
    val country_code: String? = null


    @field:SerializedName("email")
    @field:JsonField(name = arrayOf("email"))
    val email: String? = null

    @field:SerializedName("district")
    @field:JsonField(name = arrayOf("district"))
    val district: String? = null


    @field:SerializedName("path")
    @field:JsonField(name = arrayOf("path"))
    val path: String? = null

    @field:SerializedName("order_date")
    @field:JsonField(name = arrayOf("order_date"))
    val order_date: String? = null
    @field:SerializedName("order_id")
    @field:JsonField(name = arrayOf("order_id"))
    val order_id: String? = null
  @field:SerializedName("order_total")
    @field:JsonField(name = arrayOf("order_total"))
    val order_total: String? = null

    @field:SerializedName("items_count")
    @field:JsonField(name = arrayOf("items_count"))
    val items_count: String? = null

    @field:SerializedName("product_details")
    @field:JsonField(name = arrayOf("product_details"))
    val product_details: ArrayList<Productdetails>? = null

    @field:SerializedName("delivered")
    @field:JsonField(name = arrayOf("delivered"))
    val delivered: String? = null

    @field:SerializedName("ordered")
    @field:JsonField(name = arrayOf("ordered"))
    val ordered: String? = null

    @field:SerializedName("delivery_status")
    @field:JsonField(name = arrayOf("delivery_status"))
    val delivery_status: String? = null

    @field:SerializedName("payment_method")
    @field:JsonField(name = arrayOf("payment_method"))
    val payment_method: String? = null


    @field:SerializedName("shipping_cost")
    @field:JsonField(name = arrayOf("shipping_cost"))
    val shipping_cost: String? = null

    @field:SerializedName("tax")
    @field:JsonField(name = arrayOf("tax"))
    val tax: String? = null


    @field:SerializedName("billing_address")
    @field:JsonField(name = arrayOf("billing_address"))
    val billing_address: ArrayList<Billingaddress?>? = null

    @field:SerializedName("shipping_address")
    @field:JsonField(name = arrayOf("shipping_address"))
    val shipping_address: ArrayList<Shippingaddress?>? = null

    @field:SerializedName("payment_status")
    @field:JsonField(name = arrayOf("payment_status"))
    val payment_status: ArrayList<payment_status?>? = null





}