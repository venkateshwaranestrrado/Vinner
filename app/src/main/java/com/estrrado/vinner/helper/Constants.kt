package com.estrrado.vinner.helper

import com.estrrado.vinner.data.models.Region
import com.estrrado.vinner.data.models.response.AddressList

object Constants {
    const val FROM  = "FROM"
    const val HOME = "Home"
    const val WORK = "work"
    const val LOGIN_PAGE = "LoginPage"
    const val PASSWORD_DOES_NOT_MATCH = "Password does not match"
    const val EMAIL_ID_NOT_VALID = "Email id not valid"
    const val REQUIRED = "Required"
    const val SUCCESS = "success"
    const val VERIFIED = "verified"
    const val MOBILE = "Mobile"
    const val IS_LOGIN = "Is Login"
    const val ACCESS_TOKEN = "Access Token"
    const val TRUE = "true"
    const val PRODUCT_ID = "Product id"
    const val OPERATOR_ID = "Operator id"
    const val CART_ID = "Cart id"
    const val TOTAL_PAYABLE = "Total Payable"
    var regions: List<Region> = ArrayList<Region>()
    var logo = ""
    var BRAND_ID = ""
    var ADDRESS = ""
    const val ROAD_NAME = "ROAD_NAME"
    const val ADDDRESS_TYPE = "ADDDRESS_TYPE"
    const val LANDMARK = "LANDMARK"
    const val NAME = "NAME"
    const val CITY = "CITY"
    const val COUNTRY = "COUNTRY"
    const val PHONE = "PHONE"
    const val IS_DEFAULT = "IS_DEFAULT"
    const val IS_EDIT = "isEdit"
    const val PINCODE = "PINCODE"
    const val HOUSENAME = "HOUSENAME"
    const val ADDRESS_ID = "ADDRESS_ID"
    const val PRODUCT_NAME = "Product NAME"
    const val shareLink = "https://vinshopify.com/share"
    var addressSelected: AddressList? = null
}