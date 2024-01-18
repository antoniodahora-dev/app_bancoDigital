package com.a3tecnology.bancodigital.util

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.a3tecnology.bancodigital.R
import com.a3tecnology.bancodigital.databinding.LayoutBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

// Toolbar the Views
fun Fragment.initToolbar(
    toolbar: Toolbar,
    homeAsUpEnabled: Boolean = true,
    light: Boolean = false
) {

    val iconBack = if (light) R.drawable.ic_back_white else R.drawable.ic_back

    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    (activity as AppCompatActivity).title = ""
    (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUpEnabled)
    (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(iconBack)

    toolbar.setNavigationOnClickListener {
        activity?.onBackPressed()
    }

}

// messagem Bottom Sheet
fun Fragment.showBottomSheet(

    titleDialog: Int? = null,
    titleButton: Int? = null,
    message: String?,
    onClick: () -> Unit = {}
) {
    val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    val bottomSheetBinding: LayoutBottomSheetBinding =
        LayoutBottomSheetBinding.inflate(layoutInflater, null, false)

    bottomSheetBinding.txtBottomSheetTitle.text =
        getString(titleDialog ?: R.string.txt_title_bottom_sheet)

    bottomSheetBinding.txtBottomSheetMessage.text = message ?: getString(R.string.error_generic)

    bottomSheetBinding.btnBottomSheet.text =
        getString(titleButton ?: R.string.btn_bottom_sheet)

    bottomSheetBinding.btnBottomSheet.setOnClickListener {
        bottomSheetDialog.dismiss()
        onClick()
    }

    bottomSheetDialog.setContentView(bottomSheetBinding.root)
    bottomSheetDialog.show()
}