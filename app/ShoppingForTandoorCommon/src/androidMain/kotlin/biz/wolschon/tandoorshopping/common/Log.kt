@file:Suppress("unused")

package biz.wolschon.tandoorshopping.common


actual class Logger actual constructor() {

    actual fun v(tag: String, msg: String, tr: Throwable?) {
        if (tr == null) {
            android.util.Log.v(tag, msg)
        } else {
            android.util.Log.v(tag, msg, tr)
        }
    }

    actual fun d(tag: String, msg: String, tr: Throwable?) {
        if (tr == null) {
            android.util.Log.d(tag, msg)
        } else {
            android.util.Log.d(tag, msg, tr)
        }
    }

    actual fun i(tag: String, msg: String, tr: Throwable?) {
        if (tr == null) {
            android.util.Log.i(tag, msg)
        } else {
            android.util.Log.i(tag, msg, tr)
        }
    }

    actual fun w(tag: String, msg: String, tr: Throwable?) {
        if (tr == null) {
            android.util.Log.w(tag, msg)
        } else {
            android.util.Log.w(tag, msg, tr)
        }
    }

    actual fun e(tag: String, msg: String, tr: Throwable?) {
        if (tr == null) {
            android.util.Log.e(tag, msg)
        } else {
            android.util.Log.e(tag, msg, tr)
        }
    }
}