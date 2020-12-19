package jmapps.addmyfavoriteword.presentation.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.ActivityFlipModeBinding

class FlipModeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFlipModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_flip_mode)
    }
}