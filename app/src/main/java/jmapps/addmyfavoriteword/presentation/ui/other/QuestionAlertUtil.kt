package jmapps.addmyfavoriteword.presentation.ui.other

import android.content.Context
import android.text.Html
import androidx.appcompat.app.AlertDialog
import jmapps.addmyfavoriteword.R

class QuestionAlertUtil(private val ctx: Context, private val onClickQuestion: OnClickQuestion) {
    fun showAlertDialog(message: String) {
        AlertDialog.Builder(ctx).let {
            it.setIcon(R.drawable.ic_warning)
            it.setTitle(R.string.action_warning)
            it.setMessage(Html.fromHtml(message))
            it.setCancelable(false)
            it.setNegativeButton(ctx.getString(R.string.alert_cancel)) { dialog, _ ->
                dialog?.dismiss()
            }
            it.setPositiveButton(ctx.getString(R.string.alert_save)) { dialog, _ ->
                onClickQuestion.onClickPositive()
                dialog?.dismiss()
            }
            it.setNeutralButton(ctx.getString(R.string.alert_delete)) { dialog, _ ->
                onClickQuestion.onClickDelete()
                dialog?.dismiss()
            }
            it.setNegativeButton(ctx.getString(R.string.alert_cancel)) { dialog, _ ->
                dialog?.dismiss()
            }
            it.show()
        }
    }

    interface OnClickQuestion {
        fun onClickPositive()
        fun onClickDelete()
    }
}