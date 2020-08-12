package com.estrrado.vinner.vm

import androidx.lifecycle.ViewModel
import com.estrrado.vinner.VinnerRespository

class HomeVM (var respo: VinnerRespository) : ViewModel() {
    fun getHomeList() = respo.privateGetHomeList()

}