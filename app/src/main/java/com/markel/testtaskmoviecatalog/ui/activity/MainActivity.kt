package com.markel.testtaskmoviecatalog.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.markel.testtaskmoviecatalog.R
import com.markel.testtaskmoviecatalog.ui.fragments.home.MoviesHomeFragment
import com.markel.testtaskmoviecatalog.utils.RxErrorHandler
import com.markel.testtaskmoviecatalog.utils.openFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val rxErrorHandler: RxErrorHandler by inject()
    private var rxErrorDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openFragment(MoviesHomeFragment(), false)
    }

    override fun onStart() {
        super.onStart()
        if (null == rxErrorDisposable || false == rxErrorDisposable?.isDisposed) {
            rxErrorDisposable = rxErrorHandler.errorMessageToDisplay
                .filter { it.isNotBlank() }
                .throttleFirst(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { handleErrorMessage(it) }
                .subscribe()
        }
    }

    fun handleErrorMessage(message: String) {
        displayCustomToast(message)
    }

    private fun displayCustomToast(message: String) {
        if (message.isBlank()) return
        val layout = layoutInflater.inflate(R.layout.error_toast, null)
        val textView = layout.findViewById<TextView>(R.id.textview_message_errortoast)
        textView.text = message
        with(Toast(this)) {
            setGravity(Gravity.FILL_HORIZONTAL or Gravity.TOP, 0, 0)
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }
}