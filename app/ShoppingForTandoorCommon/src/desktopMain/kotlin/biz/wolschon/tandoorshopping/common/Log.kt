@file:Suppress("unused")

package biz.wolschon.tandoorshopping.common


actual class Logger actual constructor() {

    actual fun v(tag: String, msg: String, tr: Throwable?) {
        println("VERBOSE: $tag - $msg")
        tr?.printStackTrace(System.out)
    }

    actual fun d(tag: String, msg: String, tr: Throwable?) {
        println("DEBUG:   $tag - $msg")
        tr?.printStackTrace(System.out)
    }

    actual fun i(tag: String, msg: String, tr: Throwable?) {
        println("INFO:    $tag - $msg")
        tr?.printStackTrace(System.out)
    }

    actual fun w(tag: String, msg: String, tr: Throwable?) {
        System.err.println("WARNING: $tag - $msg")
        tr?.printStackTrace(System.err)
    }

    actual fun e(tag: String, msg: String, tr: Throwable?) {
        System.err.println("ERROR:   $tag - $msg")
        tr?.printStackTrace(System.err)
    }
}