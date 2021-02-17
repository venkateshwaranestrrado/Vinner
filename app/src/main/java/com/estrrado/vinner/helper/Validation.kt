package com.estrrado.vinner.helper

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
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

    fun printToastCenter(context: Context, message: String) {
        var toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun hideKeyboard(activity: Activity?) {
        try {
            activity?.let {
                val inputManager =
                    it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                val currentFocusedView = it.currentFocus
                if (currentFocusedView != null) {
                    inputManager.hideSoftInputFromWindow(
                        currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun openKeyboard(activity: Activity?) {
        try {
            activity?.let {
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm!!.showSoftInput(it.currentFocus, InputMethodManager.SHOW_IMPLICIT)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}