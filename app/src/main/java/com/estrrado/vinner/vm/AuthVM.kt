package com.estrrado.vinner.vm

import androidx.lifecycle.ViewModel

import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.data.models.request.Input
import com.estrrado.vinner.data.models.request.RequestModel

class AuthVM(var respo: VinnerRespository) : ViewModel() {
    fun login(input: Input) = respo.login(input)
    fun register(input: Input) = respo.register(input)
    fun verifyOTP(input: RequestModel) = respo.verifyOTP(input)
    fun home() = respo.home()
//    fun getProfile() = respo.privateGetProfile()
//    fun updateProfile(input: Input) = respo.privateUpdateProfile(input)
//    fun updatePassword(input: Input) = respo.privateUpdatePassword(input)
}