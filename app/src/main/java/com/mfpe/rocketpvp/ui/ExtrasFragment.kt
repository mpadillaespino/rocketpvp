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
import kotlinx.android.synthetic.main.fragment_extras.*

class ExtrasFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_extras, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cvRankings.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.LINK_RANKING))
            startActivity(intent)
        }
        cvFastMoves.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.LINK_FASTMOVES))
            startActivity(intent)
        }
        cvChargeMoves.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.LINK_CHARGEMOVES))
            startActivity(intent)
        }
        cvLegacyMoves.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.LINK_LEGACYMOVES))
            startActivity(intent)
        }
    }



}