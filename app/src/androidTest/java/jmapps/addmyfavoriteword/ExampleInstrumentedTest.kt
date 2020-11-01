package jmapps.addmyfavoriteword

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import jmapps.addmyfavoriteword.presentation.mvp.otherFragments.ContractInterface
import jmapps.addmyfavoriteword.presentation.mvp.otherFragments.OtherFragmentsPresenter

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest : ContractInterface.OtherView {
    private lateinit var otherFragmentsPresenter: OtherFragmentsPresenter

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("jmapps.addmyfavoriteword", appContext.packageName)
    }

    @Test
    fun useOtherMvp() {
        val list = mutableListOf<Any>(1, 2, 3)

        otherFragmentsPresenter = OtherFragmentsPresenter(this)
        otherFragmentsPresenter.defaultState()
        otherFragmentsPresenter.updateState(list)
    }

    override fun initView() {

    }

    override fun defaultState() {
        otherFragmentsPresenter.recyclerCategory()
        otherFragmentsPresenter.descriptionMain()
    }

    override fun updateState() {
        otherFragmentsPresenter.recyclerCategory()
        otherFragmentsPresenter.descriptionMain()
    }
}