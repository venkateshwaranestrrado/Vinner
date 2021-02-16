package com.estrrado.vinner.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Notification {

    @SerializedName("notify_type")
    @Expose
    var notify_type: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("desc")
    @Expose
    var desc: String? = null

    @SerializedName("ref_id")
    @Expose
    var ref_id: String? = null

    @SerializedName("created")
    @Expose
    var created: String? = null

}