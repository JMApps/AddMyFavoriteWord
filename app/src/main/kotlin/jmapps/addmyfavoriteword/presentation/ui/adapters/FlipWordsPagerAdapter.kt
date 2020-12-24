package jmapps.addmyfavoriteword.presentation.ui.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import jmapps.addmyfavoriteword.presentation.ui.fragments.FlipWordContainerFragment

class FlipWordsPagerAdapter(
    activity: AppCompatActivity,
    private val wordItemList: Int,
    private val flipModeState: Boolean) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return wordItemList
    }

    override fun createFragment(position: Int): Fragment {
        return FlipWordContainerFragment.newInstance(position + 1, flipModeState)
    }
}