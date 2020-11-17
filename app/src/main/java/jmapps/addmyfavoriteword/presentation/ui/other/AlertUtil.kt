package jmapps.addmyfavoriteword.presentation.ui.other

import android.content.Context
import android.text.Html
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import jmapps.addmyfavoriteword.R

class AlertUtil(private val ctx: Context) {
    fun showAlertDialog(message: String) {
        AlertDialog.Builder(ctx).let {
            it.setIcon(R.drawable.ic_warning)
            it.setTitle(R.string.action_warning)
            it.setMessage(Html.fromHtml(message))
            it.setCancelable(false)
            it.setNegativeButton(ctx.getString(R.string.alert_cancel)) { dialog, _ ->
                dialog?.dismiss()
            }
            it.setPositiveButton(ctx.getString(R.string.alert_ok)) { dialog, _ ->
                Toast.makeText(ctx, ctx.getString(R.string.toast_deleted), Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            }
            it.show()
        }
    }
}