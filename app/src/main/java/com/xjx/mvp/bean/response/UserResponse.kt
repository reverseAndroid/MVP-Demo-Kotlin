package com.xjx.mvp.bean.response

class UserResponse {

    private var code: String? = null
    private var msg: String? = null
    private var data: DataBean? = null

    fun getCode(): String? {
        return code
    }

    fun setCode(code: String?) {
        this.code = code
    }

    fun getMsg(): String? {
        return msg
    }

    fun setMsg(msg: String?) {
        this.msg = msg
    }

    fun getData(): DataBean? {
        return data
    }

    fun setData(data: DataBean?) {
        this.data = data
    }

    class DataBean {
        /**
         * user : {"id":0,"username":"qweqwe","password":null,"firstname":null,"lastname":null,"mobile":null,"email":null,"address":null,"portrait":null,"idcard":null,"version":null,"state":null,"preferrednotification":null,"preferreddashboardwidget":null,"language":null,"createtime":null,"lastupdatetime":null,"regionids":null,"roleid":0,"rolename":null,"regionname":null,"portraitfilename":null,"createby":null}
         * token : 46464654654564564
         */
        var user: UserBean? = null
        var token: String? = null

        class UserBean {
            /**
             * id : 296
             * username : qweqwe
             * password : null
             * firstname : null
             * lastname : null
             * mobile : null
             * email : null
             * address : null
             * portrait : null
             * idcard : null
             * version : null
             * state : null
             * preferrednotification : null
             * preferreddashboardwidget : null
             * language : null
             * createtime : null
             * lastupdatetime : null
             * regionids : null
             * roleid : 0
             * rolename : null
             * regionname : null
             * portraitfilename : null
             * createby : null
             */
            var id = 0
            var username: String? = null
            var password: Any? = null
            var firstname: Any? = null
            var lastname: Any? = null
            var mobile: Any? = null
            var email: Any? = null
            var address: Any? = null
            var portrait: Any? = null
            var idcard: Any? = null
            var version: Any? = null
            var state: Any? = null
            var preferrednotification: Any? = null
            var preferreddashboardwidget: Any? = null
            var language: Any? = null
            var createtime: Any? = null
            var lastupdatetime: Any? = null
            var regionids: Any? = null
            var roleid = 0
            var rolename: Any? = null
            var regionname: Any? = null
            var portraitfilename: Any? = null
            var createby: Any? = null
        }
    }
}