package com.example.test.ui.fragment

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.test.R
import com.example.test.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import androidx.core.app.ActivityCompat.startActivityForResult
import android.content.Intent
import android.provider.MediaStore
import android.widget.ImageView


class ProfileFragment:BaseFragment() {
    private val RESULT_LOAD_IMAGE = 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile,null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user_image.isClickable = false
        user_image.setOnClickListener{
            val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, RESULT_LOAD_IMAGE)
        }
        button_edit.setOnClickListener {
            group_profile.visibility = View.GONE
            group_edit.visibility = View.VISIBLE
            user_image.isClickable = true
        }

        button_save.setOnClickListener {
            user_image.isClickable = false
        }

        button_log_out.setOnClickListener {

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }


}