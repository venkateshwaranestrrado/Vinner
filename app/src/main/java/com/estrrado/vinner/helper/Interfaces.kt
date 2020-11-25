package com.estrrado.vinner.helper
import com.estrrado.vinner.data.models.response.Data

import retrofit2.Response

class Interfaces{

    interface Callback {
        fun additionalData(display : String?, logout: Boolean)
        fun failed(t: Throwable)
        fun success(response: okhttp3.Response?, data: Data?, state: Boolean)
    }
    interface ReturnData {
        fun getValue(key : String, value: String)
    }
    interface ReturnPos {
        fun getValue(position : String)
    }
    interface ReturnAnyWithKey {
        fun getValue(key : String, value: Any)
    }
    interface ReturnText {
        fun getValue(textValue: String)
    }
    interface ReturnAny {
        fun getValue(values: Any?)
    }

}