package jmapps.addmyfavoriteword.presentation.mvp.other

class OtherPresenter(_otherView: ContractInterface.OtherView) : ContractInterface.Presenter {

    private var otherView: ContractInterface.OtherView = _otherView
    private var model: ContractInterface.Model = OtherModel()

    override fun defaultState() {
        otherView.defaultState()
    }

    override fun recyclerCategory() = model.recyclerCategory()

    override fun descriptionMain() = model.descriptionMain()


    override fun updateState(list: MutableList<Any>) {
        model.updateState(list)
        otherView.updateState()
    }
}