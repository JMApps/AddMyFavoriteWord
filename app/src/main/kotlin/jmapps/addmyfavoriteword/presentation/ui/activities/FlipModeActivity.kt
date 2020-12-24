package jmapps.addmyfavoriteword.presentation.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.ActivityFlipModeBinding
import jmapps.addmyfavoriteword.presentation.ui.adapters.FlipWordsPagerAdapter
import jmapps.addmyfavoriteword.presentation.ui.models.WordsItemViewModel

class FlipModeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFlipModeBinding
    private lateinit var wordsItemViewModel: WordsItemViewModel
    private lateinit var flipWordsPagerAdapter: FlipWordsPagerAdapter

    private var displayBy: Long? = null
    private lateinit var flipModeItem: MenuItem

    companion object {
        const val KEY_DISPLAY_BY_FLIP_MODE = "key_display_by_flip_mode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        wordsItemViewModel = ViewModelProvider(this).get(WordsItemViewModel::class.java)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_flip_mode)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        displayBy = intent.getLongExtra(KEY_DISPLAY_BY_FLIP_MODE, 0)

        initFlipWordsMode(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_word_flip, menu)
        flipModeItem = menu.findItem(R.id.action_change_flip_mode)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()

            R.id.action_change_flip_mode -> {
                if (flipModeItem.isChecked) {
                    initFlipWordsMode(true)
                    flipModeItem.isChecked = false
                } else {
                    initFlipWordsMode(false)
                    flipModeItem.isChecked = true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initFlipWordsMode(flipMode: Boolean) {
        wordsItemViewModel.getAllWordsList(displayBy!!, "").observe(this, {
            it.let {
                flipWordsPagerAdapter = if (flipMode) {
                    FlipWordsPagerAdapter(this, it, flipMode, displayBy!!)
                } else {
                    FlipWordsPagerAdapter(this, it, flipMode, displayBy!!)
                }
                val lastCurrentItem = binding.viewPagerFlipWords.currentItem
                binding.viewPagerFlipWords.adapter = flipWordsPagerAdapter
                binding.viewPagerFlipWords.setCurrentItem(lastCurrentItem, false)
                binding.flipWordIndicator.attachToPager(binding.viewPagerFlipWords)
            }
        })
    }
}