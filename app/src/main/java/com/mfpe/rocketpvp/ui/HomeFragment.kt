package com.mfpe.rocketpvp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mfpe.rocketpvp.R
import com.mfpe.rocketpvp.utils.Constants
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    //Lifecycle ================================================================================================

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeUI()
    }


    //==========================================================================================================
    //UI Implementation ========================================================================================

    private fun initializeUI(){
        tvVersion.text = String.format(getString(R.string.homefragment_format_version),
            context!!.packageManager.getPackageInfo(context!!.packageName, 0).versionName)

        tvSendMessage.setOnClickListener{
            sendMessage()
        }
    }

    private fun sendMessage() {
        val mailto = "mailto:${Constants.MESSAGE_EMAIL}"
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse(mailto)
        startActivity(emailIntent)
    }

}