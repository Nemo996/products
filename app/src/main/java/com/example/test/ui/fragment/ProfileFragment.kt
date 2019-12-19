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
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.test.data.UserProfile
import com.example.test.utils.USER_ID
import com.example.test.view_model.MyViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel


class ProfileFragment:BaseFragment() {

    private val viewModel: MyViewModel by sharedViewModel()
    private val RESULT_LOAD_IMAGE = 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.getUser()
        return inflater.inflate(R.layout.fragment_profile,null)
    }

    var user = UserProfile(USER_ID)

    var onLogout: ((Boolean) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userInfo.observe(this, Observer {
            user = it
            user.username?.let { username->
                profile_username.text = username
            }
            user.name?.let { name->
                profile_name.text = name
            }
            if (!it.imageUri.isNullOrEmpty()){

                this.context?.let {context ->
                    Glide.with(context)
                        .load(Uri.parse(it.imageUri))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(user_image)
                }
            }

        })



        user_image.isClickable = false
        user_image.setOnClickListener{
            val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, RESULT_LOAD_IMAGE)
        }
        button_edit.setOnClickListener {
            group_profile.visibility = View.GONE
            group_edit.visibility = View.VISIBLE
            edit_profile_name.setText(profile_name.text)
            user_image.isClickable = true
        }

        button_save.setOnClickListener {
            group_profile.visibility = View.VISIBLE
            group_edit.visibility = View.GONE
            profile_name.text =edit_profile_name.text
            user_image.isClickable = false
            user.name =edit_profile_name.text.toString()
            user.username = profile_username.text.toString()
            viewModel.saveUser(user)
        }

        button_log_out.setOnClickListener {
            viewModel.cleanUser(user)
            viewModel.logOut()
           onLogout?.invoke(true)

        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null && data.data != null) {
            user.imageUri = data.data.toString()
            this.context?.let {
                Glide.with(it)
                    .load(data.data)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(user_image)
            }
        }

    }


}