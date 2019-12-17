package com.example.test.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.test.R
import com.example.test.ui.MainActivity
import com.example.test.view_model.MyViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


class LoginBottomSheetDialogFragment: BottomSheetDialogFragment() {
   private val viewModel : MyViewModel by sharedViewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val displayMetrics = activity!!.resources.displayMetrics

        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        val maxHeight = (height * 0.88).toInt()


        return inflater.inflate(R.layout.fragment_login,null).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,maxHeight)
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login_fragment_back.setOnClickListener {
            this.dismiss()
        }
        login_button.setOnClickListener {
            viewModel.login()
        }
        register_button.setOnClickListener {
            viewModel.register()
        }

    }
}