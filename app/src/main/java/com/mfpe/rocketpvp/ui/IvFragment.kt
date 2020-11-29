package com.mfpe.rocketpvp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mfpe.rocketpvp.R
import com.mfpe.rocketpvp.models.*
import com.mfpe.rocketpvp.utils.Common
import com.mfpe.rocketpvp.utils.Constants
import kotlinx.android.synthetic.main.fragment_iv.*
import kotlin.math.floor
import kotlin.math.pow



class IvFragment : Fragment() {

    private lateinit var pokedex: Pokedex
    private var cpmultiple2: List<Double> = emptyList()
    private var cpmultiple4: List<Double> = emptyList()

    //Lifecycle ================================================================================================

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        pokedex = Common.getPokedex(context!!)

        cpmultiple2 = Constants.cpmultiple.map { it.pow(2) }
        cpmultiple4 = cpmultiple2.map { it.pow(2) }

        return inflater.inflate(R.layout.fragment_iv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeUI()
        loadPokedexSuggestions()
        loadLeagues()
        loadIvRanges()
        loadIvs()
    }

    //==========================================================================================================
    //UI Implementation ========================================================================================

    private fun initializeUI(){
        btnCalculate.setOnClickListener{
            calculateIv()
        }
    }

    private fun loadPokedexSuggestions() {
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, pokedex.pokemons)
        etPokemon.setAdapter(adapter)

        etPokemon.setOnItemClickListener { _, _, _, _ ->
            val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(etPokemon.windowToken, 0)
        }
    }

    private fun loadLeagues() {
        val adapter =
            ArrayAdapter(context!!, android.R.layout.simple_spinner_item, Common.getLeagues())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLeagues.adapter = adapter
    }

    private fun loadIvRanges() {
        val adapter =
            ArrayAdapter(context!!, android.R.layout.simple_spinner_item, Common.getIvRanges())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMiniv.adapter = adapter
    }

    private fun loadIvs() {
        val ivs = Common.getIvs()

        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, ivs)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerAtk.adapter = adapter
        spinnerDef.adapter = adapter
        spinnerHp.adapter = adapter
    }


    //==========================================================================================================
    //Inner Methods ============================================================================================

    private fun calculateIv() {
        val pokemon = findPokemonByName(etPokemon.text.toString())

        if (pokemon == null) {
            Toast.makeText(context, getString(R.string.ivfragment_toast_pokemonnotfound), Toast.LENGTH_SHORT).show()
            return
        }

        var myPokemonRank = 0
        val ivCombos = arrayListOf<IvCombo>()
        val ivProducts = arrayListOf<IvProduct>()
        val maxCp = (spinnerLeagues.selectedItem as League).maxCp
        val ivRange = (spinnerMiniv.selectedItem as IvRange).value
        val ivAtk = spinnerAtk.selectedItem.toString().toInt()
        val ivDef = spinnerDef.selectedItem.toString().toInt()
        val ivHp = spinnerHp.selectedItem.toString().toInt()


        //Calculando productos
        for (i in ivRange..15) {
            for (j in ivRange..15) {
                for (k in ivRange..15) {
                    ivCombos.add(IvCombo(i, j, k))
                }
            }
        }

        for (i in 0 until ivCombos.size) {
            val atk = pokemon.atk + ivCombos[i].atk
            val def = pokemon.def + ivCombos[i].def
            val hp = pokemon.hp + ivCombos[i].hp
            val level = levelCap(atk, def, hp, maxCp, cpmultiple4)
            val statProduct = cpmultiple2[level] * atk * def * floor(Constants.cpmultiple[level] * hp)
            ivProducts.add(IvProduct(statProduct, level, ivCombos[i].atk, ivCombos[i].def, ivCombos[i].hp))
        }

        ivProducts.sortByDescending { it.statProduct }

        //Calculando rank del pokemon
        for(i in 0 until ivProducts.size) {
            if(ivProducts[i].atk == ivAtk && ivProducts[i].def == ivDef && ivProducts[i].hp == ivHp) {
                myPokemonRank = i + 1
            }
        }

        val at = pokemon.atk + ivAtk
        val df = pokemon.def + ivDef
        val st = pokemon.hp + ivHp
        val lvl = levelCap(at, df, st, maxCp, cpmultiple4)
        val stat = cpmultiple2[lvl] * at * df * floor(Constants.cpmultiple[lvl] * st)

        if (myPokemonRank == 0) {
            for(i in 0..ivProducts.size) {
                if(ivProducts[i].statProduct == stat) {
                    myPokemonRank = i
                }
            }
        }

        val intent = Intent(context, IvResultsActivity::class.java)
        intent.putExtra(IvResultsActivity.EXTRA_POKEMON, pokemon)
        intent.putExtra(IvResultsActivity.EXTRA_RESULTS, ArrayList(ivProducts.take(Constants.resultsCount)))
        intent.putExtra(IvResultsActivity.EXTRA_CPM2, cpmultiple2.toDoubleArray())
        intent.putExtra(IvResultsActivity.EXTRA_LEAGUENAME, (spinnerLeagues.selectedItem as League).name)
        intent.putExtra(IvResultsActivity.EXTRA_MYRANK, myPokemonRank)
        intent.putExtra(IvResultsActivity.EXTRA_MYIVS, String.format(context!!.getString(R.string.ivresults_format_iv), ivAtk, ivDef, ivHp))
        intent.putExtra(IvResultsActivity.EXTRA_MYLEVEL,  Common.getRealLevel(lvl))
        intent.putExtra(IvResultsActivity.EXTRA_MYCP, Common.getCp(at, df, st, cpmultiple2[lvl]))

        startActivity(intent)
    }

    private fun findPokemonByName(name: String): Pokemon? {
        return pokedex.pokemons.find { it.name == name }
    }

    private fun levelCap(atk: Int, def: Int, hp: Int, targetCp: Int, cpm4: List<Double>): Int {
        val cap = binarySearch(cpm4, (targetCp + 1) * (targetCp + 1) * 100L / (atk.toLong() * atk.toLong() * def.toLong() * hp.toLong()).toDouble())
        return if (cap > Constants.maxLevel) Constants.maxLevel else cap
    }

    private fun binarySearch(array: List<Double>, num: Double): Int {
        var m = 0
        var n = array.size - 1
        while (m <= n) {
            val k = n + m shr 1
            when {
                array[k] < num -> m = k + 1
                array[k] > num -> n = k - 1
                else -> return k
            }
        }
        return m - 1
    }

}