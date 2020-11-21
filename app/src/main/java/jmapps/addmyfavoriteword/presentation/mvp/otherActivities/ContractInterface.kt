package jmapps.addmyfavoriteword.presentation.mvp.otherActivities

interface ContractInterface {

    interface OtherView {
        fun initView(displayBy: Long, orderBy: String)
        fun defaultState()
        fun updateState()
    }

    interface Model {
        fun recyclerCategory() : Int
        fun descriptionMain() : Int
        fun updateState(list: List<Any>)
    }

    interface Presenter {
        fun initView(displayBy: Long, orderBy: String)
        fun defaultState()
        fun recyclerCategory() : Int
        fun descriptionMain() : Int
        fun updateState(list: List<Any>)
    }
}