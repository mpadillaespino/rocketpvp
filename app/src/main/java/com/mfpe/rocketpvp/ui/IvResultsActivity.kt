package com.mfpe.rocketpvp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mfpe.rocketpvp.R
import com.mfpe.rocketpvp.adapters.IvResultsAdapter
import com.mfpe.rocketpvp.models.IvProduct
import com.mfpe.rocketpvp.models.Pokemon
import kotlinx.android.synthetic.main.activity_iv_results.*

class IvResultsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_POKEMON = "EXTRA_POKEMON"
        const val EXTRA_RESULTS = "EXTRA_RESULTS"
        const val EXTRA_CPM2 = "EXTRA_CPM2"
        const val EXTRA_LEAGUENAME = "EXTRA_LEAGUENAME"
        const val EXTRA_MYRANK = "EXTRA_MYRANK"
        const val EXTRA_MYIVS = "EXTRA_MYIVS"
        const val EXTRA_MYCP = "EXTRA_MYCP"
        const val EXTRA_MYLEVEL = "EXTRA_MYLEVEL"
    }

    private lateinit var results: List<IvProduct>
    private lateinit var pokemon: Pokemon
    private lateinit var cpm2: List<Double>
    private lateinit var leagueName: String
    private var myRank: Int = 0
    private lateinit var myIvs: String
    private var myLevel: Double = 0.0
    private var myCp: Int = 0
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var resultsAdapter: RecyclerView.Adapter<IvResultsAdapter.ViewHolder>? = null

    //Lifecycle ================================================================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iv_results)

        results = intent.getParcelableArrayListExtra(EXTRA_RESULTS)
        pokemon = intent.getParcelableExtra(EXTRA_POKEMON)
        cpm2 = intent.getDoubleArrayExtra(EXTRA_CPM2).asList()
        leagueName = intent.getStringExtra(EXTRA_LEAGUENAME)
        myRank = intent.getIntExtra(EXTRA_MYRANK, 0)
        myIvs = intent.getStringExtra(EXTRA_MYIVS)
        myLevel = intent.getDoubleExtra(EXTRA_MYLEVEL, 0.0)
        myCp = intent.getIntExtra(EXTRA_MYCP, 0)

        initializeUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.nav_share -> shareMyPokemonInfo()
            else -> super.onBackPressed()
        }
        return true
    }

    //==========================================================================================================
    //UI Implementation ========================================================================================

    private fun initializeUI() {
        supportActionBar?.title = getString(R.string.ivresults_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadResults(results)
        tvPokemon.text = pokemon.name
        tvLeague.text = leagueName
        tvIvs.text = myIvs
        tvRank.text = String.format(getString(R.string.ivresults_format_rank), myRank)
        tvCp.text = String.format(getString(R.string.ivresults_format_cplvl), myCp, myLevel)
    }

    private fun loadResults(results: List<IvProduct>) {
        layoutManager = LinearLayoutManager(this)
        rvResults.layoutManager = layoutManager

        resultsAdapter = IvResultsAdapter(this, pokemon, cpm2, results)
        rvResults.adapter = resultsAdapter
    }


    //==========================================================================================================
    //Inner Methods ============================================================================================

    private fun shareMyPokemonInfo() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                String.format(
                    getString(R.string.ivresults_share_pokemoninfo_template),
                    pokemon.name,
                    myRank,
                    leagueName,
                    myIvs,
                    String.format(getString(R.string.ivresults_format_cplvl), myCp, myLevel)
                )
            )
            type = "text/plain"
        }
        startActivity(sendIntent)
    }

}