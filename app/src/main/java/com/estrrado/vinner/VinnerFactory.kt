package com.estrrado.vinner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VinnerFactory(private var model: Any) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = model as T
}
