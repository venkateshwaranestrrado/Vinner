package com.estrrado.vinner.data.models.response

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

    @SerializedName("email")
    @Expose
    private var email: String? = null

    @SerializedName("mobile")
    @Expose
    private var mobile: String? = null

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

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun getMobile(): String? {
        return mobile
    }

    fun setMobile(mobile: String?) {
        this.mobile = mobile
    }

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
    private var relatedProducts: List<Any?>? = null

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

    fun getRelatedProducts(): List<Any?>? {
        return relatedProducts
    }

    fun setRelatedProducts(relatedProducts: List<Any?>?) {
        this.relatedProducts = relatedProducts
    }

    @SerializedName("cart")
    @Expose
    private var cart: Cart? = null

    @SerializedName("items_total")
    @Expose
    private var itemsTotal: Int? = null

    @SerializedName("cart_items")
    @Expose
    private var cartItems: List<CartItem?>? = null

    @SerializedName("address")
    @Expose
    private var address: String? = null

    fun getCart(): Cart? {
        return cart
    }

    fun setCart(cart: Cart?) {
        this.cart = cart
    }

    fun getItemsTotal(): Int? {
        return itemsTotal
    }

    fun setItemsTotal(itemsTotal: Int?) {
        this.itemsTotal = itemsTotal
    }

    fun getCartItems(): List<CartItem?>? {
        return cartItems
    }

    fun setCartItems(cartItems: List<CartItem?>?) {
        this.cartItems = cartItems
    }

    fun getAddress(): String? {
        return address
    }

    fun setAddress(address: String?) {
        this.address = address
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


}