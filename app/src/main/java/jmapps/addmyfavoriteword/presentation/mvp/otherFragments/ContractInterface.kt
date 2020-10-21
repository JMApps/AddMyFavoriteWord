package jmapps.addmyfavoriteword.presentation.mvp.otherFragments

interface ContractInterface {

    interface OtherView {
        fun defaultState()
        fun updateState()
    }

    interface Model {
        fun recyclerCategory() : Int
        fun descriptionMain() : Int
        fun updateState(list: MutableList<Any>)
    }

    interface Presenter {
        fun defaultState()
        fun recyclerCategory() : Int
        fun descriptionMain() : Int
        fun updateState(list: MutableList<Any>)
    }
}