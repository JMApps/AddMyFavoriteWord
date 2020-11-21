package jmapps.addmyfavoriteword.presentation.mvp.otherActivities

class OtherActivityPresenter(_otherView: ContractInterface.OtherView) :
    ContractInterface.Presenter {

    private var otherView: ContractInterface.OtherView = _otherView
    private var model: ContractInterface.Model = OtherActivityModel()

    override fun initView(dysplayBy: Long, orderBy: String) {
        otherView.initView(dysplayBy, orderBy)
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