package com.estrrado.vinner.helper

import com.estrrado.vinner.data.models.Region
import com.estrrado.vinner.data.models.response.AddressList

object Constants {

    const val PROFILE_IMAGE = "IMAGE"
    const val PROFILENAME = "PROFILENAME"
    const val PROFILEMAIL = "PROFILEMAIL"

    const val FROM = "FROM"
    const val HOME = "Home"
    const val WORK = "work"
    const val LOGIN_PAGE = "LoginPage"
    const val PASSWORD_DOES_NOT_MATCH = "Password does not match"
    const val EMAIL_ID_NOT_VALID = "Email id not valid"
    const val REQUIRED = "Required"
    const val SUCCESS = "success"
    const val VERIFIED = "verified"
    const val MOBILE = "Mobile"
    const val IS_LOGIN = "IsLogin"
    var FROM_LOGIN = 0
    const val ACCESS_TOKEN = "Access Token"
    const val TRUE = "true"
    const val PRODUCT_ID = "Product id"
    const val OPERATOR_ID = "Operator id"
    const val CART_ID = "Cart id"
    const val TOTAL_PAYABLE = "Total Payable"
    var regions: List<Region> = ArrayList<Region>()
    var logo = ""
    var BRAND_ID = ""
    var ADDRESS = "ADDRESS"

    const val CONTACTNO = "CONTACTNO"
    const val BUILDINGNAME = "BUILDINGNAME"

    const val ROAD_NAME = "ROAD_NAME"
    const val ADDDRESS_TYPE = "ADDDRESS_TYPE"
    const val LANDMARK = "LANDMARK"
    const val NAME = "NAME"
    const val EMAIL = "EMAIL"
    const val CCURRENCY = "CCURRENCY"
    const val CITY = "CITY"
    const val COUNTRY = "COUNTRY"
    const val COUNTRY_NAME = "COUNTRY_NAME"
    const val PHONE = "PHONE"
    const val IS_DEFAULT = "IS_DEFAULT"
    const val IS_EDIT = "isEdit"
    const val PINCODE = "PINCODE"
    const val HOUSENAME = "HOUSENAME"
    const val ADDRESS_ID = "ADDRESS_ID"
    const val PRODUCT_NAME = "Product NAME"
    const val STATUS = "Status"
    const val NOT_SERVING_IN_THIS_REGION = "Not serving in this location"
    const val DELIVERED = "delivered"
    const val VIEW_REVIEW = "View Review"
    const val WRITE_A_REVIEW = "Write a Review"
    const val PENDING = "pending"
    const val ON_DELIVERY = "on-delivery"
    const val shareLink = "https://vinshopify.com/"
    var addressSelected: AddressList? = null
    var shipAddressSelected: AddressList? = null
    val reqCode: Int = 1
    val DO_YOU_CONFIRM_TO_CHECK_OUT: String = "Do you confirm to check out the items?"

    var S_ADDRESS = "S_ADDRESS"
    var S_PINCODE = "S_PINCODE"
    var S_HOUSENAME = "S_HOUSENAME"
    var S_LANDMARK = "S_LANDMARK"
    var S_ADDDRESS_TYPE = "S_ADDDRESSTYPE"
    var S_ROAD_NAME = "S_ROADNAME"
    var S_COUNTRY = "S_COUNTRY"
    var S_CITY = "S_CITY"
    var S_NAME = "S_NAME"
    var S_EMAIL = "S_EMAIL"
    var S_CONTACTNO = "S_CONTACTNO"
    var S_BUILDINGNAME = "S_BUILDINGNAME"

}