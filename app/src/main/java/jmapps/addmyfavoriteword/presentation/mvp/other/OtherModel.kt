package jmapps.addmyfavoriteword.presentation.mvp.other

import android.view.View

class OtherModel : ContractInterface.Model {

    private var recycler = View.GONE
    private var description = View.VISIBLE

    override fun recyclerCategory(): Int {
        return recycler
    }

    override fun descriptionMain(): Int {
        return description
    }

    override fun updateState(list: MutableList<Any>) {
        if (list.isNotEmpty()) {
            recycler = View.VISIBLE
            description = View.GONE
        } else {
            recycler = View.GONE
            description = View.VISIBLE
        }
    }
}