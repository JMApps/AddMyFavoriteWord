package jmapps.addmyfavoriteword.presentation.mvp.otherFragments

import android.view.View

class OtherFragmentsModel : ContractInterface.Model {

    private var recycler = View.GONE
    private var description = View.VISIBLE

    override fun recyclerCategory(): Int {
        return recycler
    }

    override fun descriptionMain(): Int {
        return description
    }

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