package jmapps.addmyfavoriteword.presentation.ui.other

import android.content.Context
import android.text.Html
import androidx.appcompat.app.AlertDialog
import jmapps.addmyfavoriteword.R

class QuestionAlertUtil(private val ctx: Context, private val onClickQuestion: OnClickQuestion) {
    fun showAlertDialog(message: String, positive: String, negative: String) {
        AlertDialog.Builder(ctx).let {
            it.setIcon(R.drawable.ic_warning)
            it.setTitle(R.string.action_warning)
            it.setMessage(Html.fromHtml(message))
            it.setCancelable(false)
            it.setPositiveButton(positive) { dialog, _ ->
                onClickQuestion.onClickPositive()
                dialog?.dismiss()
            }
            it.setNegativeButton(negative) { dialog, _ ->
                onClickQuestion.onClickDelete()
                dialog?.dismiss()
            }
            it.setNeutralButton(ctx.getString(R.string.alert_cancel)) { dialog, _ ->
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