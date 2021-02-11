package com.estrrado.vinner.helper

import android.content.Context
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import java.util.regex.Pattern

object Validation {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    fun EditText.validate(): Boolean {
        if (text.isEmpty() || text.isBlank()) {
            error = "Required"
            return false
        }
        return true
    }

    fun hasText(editText: TextView?, error: String): Boolean {
        if (editText != null) {
            val text = editText.text.toString().trim { it <= ' ' }
            editText.error = null
            if (text.isEmpty()) {
                editText.error = error
                return false
            }
            return true
        } else {
            return false
        }
    }

    fun TextInputEditText.validate(): Boolean {
        if (text!!.isEmpty() || text!!.isBlank()) {
            error = "Required"
            return false
        }
        return true
    }


    fun TextInputEditText.isValidEmail(): Boolean {
        if (validate()) {
            val text = text.toString().trim { it <= ' ' }
            error = null                        //clear previous error message
            if (!Pattern.matches(emailPattern, text)) {
                requestFocus()
                error = "Invalid Email Address"
                return false
            }
            return true
        }
        return false
    }

    fun printToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}