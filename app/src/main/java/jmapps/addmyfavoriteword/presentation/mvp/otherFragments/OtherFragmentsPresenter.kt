package jmapps.addmyfavoriteword.presentation.mvp.otherFragments

class OtherFragmentsPresenter(_otherView: ContractInterface.OtherView) :
    ContractInterface.Presenter {

    private var otherView: ContractInterface.OtherView = _otherView
    private var model: ContractInterface.Model = OtherFragmentsModel()

    override fun initView() {
        otherView.initView()
    }

    override fun defaultState() {
        otherView.defaultState()
    }

    override fun recyclerCategory() = model.recyclerCategory()

    override fun descriptionMain() = model.descriptionMain()

    override fun updateState(list: List<Any>) {
        model.updateState(list)
        otherView.updateState()
    }
}