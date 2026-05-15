package com.uzuu.learn16_hilt_dependencyinject

import javax.inject.Inject

class UserRepository @Inject constructor(){
    fun getName(): String {
        return "Kết quả sẽ hiển thị ở đây!"
    }
}