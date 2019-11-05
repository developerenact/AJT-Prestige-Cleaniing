package com.android.ajtprestigecleaning.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import io.paperdb.Paper
import android.view.Gravity
import android.view.Window
import android.view.Window.FEATURE_NO_TITLE
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import com.android.ajtprestigecleaning.R
import com.squareup.picasso.Picasso
import java.util.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


abstract class BaseActivity : AppCompatActivity() {

    abstract fun getLayoutResourceId(): Int
    var  progressBar: Dialog? = null

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResourceId())
        Paper.init(applicationContext);
        initProgress()

    }



    fun showProgress() {
        if (progressBar == null) {
            initProgress()
        }

        if(progressBar?.isShowing!!){
            hideProgress()
        }

        try {
            if (!isFinishing) {
                progressBar!!.show()
            }
        } catch (e: Exception) {
            Log.e("",""+e.localizedMessage)
        }


    }

    fun hideProgress() {
        if (progressBar == null) {
            initProgress()
        }
        progressBar!!.dismiss()
    }




    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    fun hideSystemUI() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

    }

    fun showAlert(message: String) {

        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setMessage(message)
            setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
            show()
        }
    }

    private val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        dialog.dismiss()
    }



    fun customDialog(message: String, context: Context) {

        val dialog = Dialog(context)
            dialog.setContentView(R.layout.ios_dialog);
            dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            dialog.setTitle("Title...");
            dialog.setCancelable(false)
            val ok = dialog.findViewById(R.id.ios_ok) as TextView
            val text = dialog.findViewById(R.id.ios_text_logout) as TextView
            text.text = message

            ok.setOnClickListener {
                dialog.dismiss()

            }

            dialog.show()

    }


    fun initProgress() {
        progressBar = Dialog(this)
       // window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        progressBar!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressBar!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressBar!!.setContentView(R.layout.loader_layout)
        progressBar!!.getWindow().setBackgroundDrawableResource(android.R.color.transparent)
        progressBar!!.getWindow().setGravity(Gravity.CENTER)
        progressBar!!.setCancelable(false)
    }


    @SuppressLint("WrongConstant")
    private fun showMessage(isConnected: Boolean) {


        if (!isConnected) {

            val messageToUser = "You are offline now." //TODO
            // Toast.makeText(this,getString(R.string.wentwrong),Toast.LENGTH_SHORT).show()

//           iosAlert(getString(R.string.wentwrong),applicationContext)

            /* mSnackBar = Snackbar.make(findViewById(R.id.root), messageToUser, Snackbar.LENGTH_LONG) //Assume "rootLayout" as the root layout of every activity.
             mSnackBar?.duration = Snackbar.LENGTH_SHORT
             mSnackBar?.show()*/
            // showSnackBar("You are offline now.")
        } else {
            //  mSnackBar?.dismiss()

        }


    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }


/*
    fun customDialog(activity: Activity, message: String) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.ios_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val text = dialog.findViewById(R.id.ios_text_logout) as TextView
        text.text = message
        val dialogButton = dialog.findViewById(R.id.ios_ok) as TextView
        dialogButton.setOnClickListener { dialog.dismiss() }

        try {
            dialog.show()
        } catch (e: Exception) {
            dialog.dismiss()
        }


    }
*/


    fun forgotPassCustomDialog(message: String, context: Context) {

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.forgotpass_dialog);
        dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setTitle("Title...");
        dialog.setCancelable(false)
        val ok = dialog.findViewById(R.id.ios_ok) as TextView
        val text = dialog.findViewById(R.id.ios_text_logout) as TextView
        text.text = message

        ok.setOnClickListener {
            finish()
        }

        dialog.show()

    }




}