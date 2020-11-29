package com.mfpe.rocketpvp.ui

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mfpe.rocketpvp.R
import com.mfpe.rocketpvp.utils.Common
import com.mfpe.rocketpvp.utils.Constants
import kotlinx.android.synthetic.main.fragment_cooldown.*

class CooldownFragment : Fragment() {

    //Lifecycle ================================================================================================

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cooldown, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeUI()
    }


    //==========================================================================================================
    //UI Implementation ========================================================================================

    private fun initializeUI() {
        btnPasteA.setOnClickListener{
            etlocationa.setText(Common.getCoordinatesFromClipboard(context!!))
        }

        btnPasteB.setOnClickListener{
            etlocationb.setText(Common.getCoordinatesFromClipboard(context!!))
        }

        btnCalculate.setOnClickListener{
            calculateDistance()
        }
    }

    //==========================================================================================================
    //Inner Methods ============================================================================================

    private fun calculateDistance() {
        if(!validateEntries()) return

        //Calculando distancia
        val latlongA = etlocationa.text.toString().replace(" ","").split(",")
        val pointA = Location("")
        pointA.latitude = latlongA[0].toDouble()
        pointA.longitude = latlongA[1].toDouble()

        val latlongB = etlocationb.text.toString().replace(" ","").split(",")
        val pointB = Location("")
        pointB.latitude = latlongB[0].toDouble()
        pointB.longitude = latlongB[1].toDouble()

        val distance = pointA.distanceTo(pointB) / 1000
        val cooldown = Common.getCooldown(distance)
        tvDistanceDesc.text = getString(R.string.cdfragment_location_calculated)
        tvDistance.text = String.format(getString(R.string.cdfragment_location_format_distance), Common.formatDecimals(distance.toDouble()))
        tvCooldown.text = String.format(getString(R.string.cdfragment_location_format_cooldown), cooldown)
    }

    private fun validateEntries(): Boolean{
        if(!Constants.LATLONG_REGEX.toRegex().matches(etlocationa.text.toString())){
            Toast.makeText(context, getString(R.string.cdfragment_toast_locationa_invalid), Toast.LENGTH_SHORT).show()
            return false
        }

        if(!Constants.LATLONG_REGEX.toRegex().matches(etlocationb.text.toString())){
            Toast.makeText(context, getString(R.string.cdfragment_toast_locationb_invalid), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}