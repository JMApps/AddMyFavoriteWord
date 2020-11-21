package jmapps.addmyfavoriteword.presentation.mvp.otherActivities

import android.view.View

class OtherActivityModel : ContractInterface.Model {

    private var recycler = View.GONE
    private var description = View.VISIBLE

    override fun recyclerCategory() = recycler

    override fun descriptionMain() = description

    override fun updateState(list: List<Any>) {
        if (list.isNotEmpty()) {
            recycler = View.VISIBLE
            description = View.GONE
        } else {
            recycler = View.GONE
            description = View.VISIBLE
        }
    }
}