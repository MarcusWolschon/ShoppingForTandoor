package biz.wolschon.tandoorshopping.common


expect class Logger() {

    fun v(tag: String, msg: String, tr: Throwable? = null)
    fun d(tag: String, msg: String, tr: Throwable? = null)
    fun i(tag: String, msg: String, tr: Throwable? = null)
    fun w(tag: String, msg: String, tr: Throwable? = null)
    fun e(tag: String, msg: String, tr: Throwable? = null)
}

val Log = Logger()