package com.mfpe.rocketpvp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mfpe.rocketpvp.R
import com.mfpe.rocketpvp.models.IvProduct
import com.mfpe.rocketpvp.models.Pokemon
import com.mfpe.rocketpvp.utils.Common

class IvResultsAdapter(
    private val context: Context,
    private val pokemon: Pokemon,
    private val cpm2: List<Double>,
    private val results: List<IvProduct>
) : RecyclerView.Adapter<IvResultsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.iv_results_item, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvRank: TextView? = null
        var tvIvs: TextView? = null
        var tvLvl: TextView? = null
        var tvCp: TextView? = null
        var tvPercent: TextView? = null

        init {
            tvRank = itemView.findViewById(R.id.tvRank)
            tvIvs = itemView.findViewById(R.id.tvIvs)
            tvLvl = itemView.findViewById(R.id.tvLvl)
            tvCp = itemView.findViewById(R.id.tvCp)
            tvPercent = itemView.findViewById(R.id.tvPercent)
        }
    }

    override fun getItemCount(): Int = results.size + 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {

            setHeaderBg(holder.tvRank!!)
            setHeaderBg(holder.tvIvs!!)
            setHeaderBg(holder.tvLvl!!)
            setHeaderBg(holder.tvCp!!)
            setHeaderBg(holder.tvPercent!!)

            holder.tvRank!!.text = context.getString(R.string.ivresults_rank)
            holder.tvIvs!!.text = context.getString(R.string.ivresults_ivs)
            holder.tvLvl!!.text = context.getString(R.string.ivresults_level)
            holder.tvCp!!.text = context.getString(R.string.ivresults_cp)
            holder.tvPercent!!.text = context.getString(R.string.ivresults_percent)
        } else {

            setCellBg(holder.tvRank!!)
            setCellBg(holder.tvIvs!!)
            setCellBg(holder.tvLvl!!)
            setCellBg(holder.tvCp!!)
            setCellBg(holder.tvPercent!!)

            holder.tvRank!!.text = (position).toString()
            holder.tvIvs!!.text = String.format(
                context.getString(R.string.ivresults_format_iv),
                results[position - 1].atk,
                results[position - 1].def,
                results[position - 1].hp
            )
            holder.tvLvl!!.text = (Common.getRealLevel(results[position - 1].level)).toString()
            holder.tvCp!!.text = Common.getCp(
                pokemon.atk + results[position - 1].atk,
                pokemon.def + results[position - 1].def,
                pokemon.hp + results[position - 1].hp,
                cpm2[results[position - 1].level]
            ).toString()
            holder.tvPercent!!.text = String.format(
                context.getString(R.string.ivresults_format_percent),
                Common.formatDecimals(((results[position - 1].statProduct / results[0].statProduct * 100000) / 1000))
            )
        }
    }

    private fun setHeaderBg(view: TextView) {
        view.setBackgroundResource(R.drawable.table_header_background)
        view.setTextColor(context.getColor(R.color.white))
    }

    private fun setCellBg(view: TextView) {
        view.setBackgroundResource(R.drawable.table_cell_background)
        view.setTextColor(context.getColor(R.color.primaryText))
    }

}