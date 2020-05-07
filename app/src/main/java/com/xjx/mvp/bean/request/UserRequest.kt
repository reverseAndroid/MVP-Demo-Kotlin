package com.xjx.mvp.bean.request

class UserRequest {

    /**
     * user : {"username":"username","password":"123456"}
     */
    private var user: UserBean? = null

    fun getUser(): UserBean? {
        return user
    }

    fun setUser(user: UserBean?) {
        this.user = user
    }

    class UserBean {
        /**
         * username : username
         * password : 123456
         */
        var username: String? = null
        var password: String? = null

    }
}