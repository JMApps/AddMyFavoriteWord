package jmapps.addmyfavoriteword.presentation.ui.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import jmapps.addmyfavoriteword.data.database.room.dictionary.words.WordItems
import jmapps.addmyfavoriteword.presentation.ui.fragments.FlipWordContainerFragment

class FlipWordsPagerAdapter(
    activity: AppCompatActivity,
    private val wordItemList: MutableList<WordItems>,
    private val flipModeState: Boolean,
    private val sectionId: Long) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return wordItemList.size
    }

    override fun createFragment(position: Int): Fragment {
        return FlipWordContainerFragment.newInstance(position + 1, flipModeState, sectionId)
    }
}