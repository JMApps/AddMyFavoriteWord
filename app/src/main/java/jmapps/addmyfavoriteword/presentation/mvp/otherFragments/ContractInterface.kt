package jmapps.addmyfavoriteword.presentation.mvp.otherFragments

interface ContractInterface {

    interface OtherView {
        fun initView()
        fun defaultState()
        fun updateState()
    }

    interface Model {
        fun recyclerCategory() : Int
        fun descriptionMain() : Int
        fun updateState(list: List<Any>)
    }

    interface Presenter {
        fun initView()
        fun defaultState()
        fun recyclerCategory() : Int
        fun descriptionMain() : Int
        fun updateState(list: List<Any>)
    }
}