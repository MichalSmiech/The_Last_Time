package com.michasoft.thelasttime.view.bottomSheet

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.michasoft.thelasttime.LastTimeApplication
import com.michasoft.thelasttime.view.LoginActivity

/**
 * Created by mśmiech on 03.05.2022.
 */
abstract class UserSessionBottomSheetDialogFragment: BottomSheetDialogFragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if((requireActivity().application as LastTimeApplication).userSessionComponent == null) {
            dismissAllowingStateLoss()
        } else {
            onFragmentAttach(context)
        }
    }

    open fun onFragmentAttach(context: Context) {
    }
}