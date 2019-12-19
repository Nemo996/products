package com.example.test.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.test.R
import com.example.test.view_model.MyViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.regex.Matcher
import java.util.regex.Pattern


class LoginBottomSheetDialogFragment: BottomSheetDialogFragment() {
   private val viewModel : MyViewModel by sharedViewModel()

    val myObserver = Observer<Boolean> {
        Log.d("Mylog","success $it")
        if(it){
            dismiss()
        }
    }
    var onDismiss: ((Unit) -> Unit)? = null
    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onDismiss?.invoke(Unit)
    }
    override fun onDismiss(dialog: DialogInterface) {

        super.onDismiss(dialog)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val displayMetrics = activity!!.resources.displayMetrics

        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        val maxHeight = (height * 0.88).toInt()

        viewModel.userIsLoggedIn.observe(this, myObserver)


        return inflater.inflate(R.layout.fragment_login,null).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,maxHeight)
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login_fragment_back.setOnClickListener {
           onBackPressed()

        }
        login_button.setOnClickListener {
            if (validateLogin(login_username) && validateLogin(login_password)){
                viewModel.login(login_username.text.toString(),login_password.text.toString())
            }
        }
        register_button.setOnClickListener {
            if (validateLogin(login_username) && validateLogin(login_password) ){
                textInputLayout3.visibility = View.VISIBLE
                textInputLayout4.visibility = View.VISIBLE
                login_button.visibility = View.GONE

                if (login_password.text.toString() == login_repeat_password.text.toString() && validateLogin(login_name)){
                    viewModel.register(login_username.text.toString(),login_password.text.toString(),login_name.text.toString())
                }else{
                    login_repeat_password.error = resources.getString(R.string.error_passwords_is_not_match)
                }

            }

        }

    }

    fun onBackPressed(){
        if (textInputLayout3.visibility == View.VISIBLE){
            textInputLayout3.visibility = View.GONE
            textInputLayout4.visibility = View.GONE
            login_button.visibility = View.VISIBLE
        }else{
            this.dismiss()
        }
    }

    fun validateLogin(textView: TextView):Boolean{


        if (textView.text.isNullOrEmpty()){
            textView.error = resources.getString(R.string.error_field_is_ampty)
            return false
        }
        if (textView.text.length<3){
            textView.error = resources.getString(R.string.error_too_short_word)
            return false
        }
        if (textView.text.length>20){
            textView.error = resources.getString(R.string.error_long_short_word)
            return false
        }
        if (!patternValidation(textView.text) && textView.id != login_name.id){
            textView.error = resources.getString(R.string.error_invalid_input)
            return false
        }

        return true
    }

    fun patternValidation(text: CharSequence):Boolean{

        val expression = "^[A-Za-z]*\$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(text)

        return matcher.matches()
    }
}