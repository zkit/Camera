package io.github.zkit.ui.beans.annotations

import android.app.Activity
import androidx.annotation.IntDef

@IntDef(flag = true, value = [Activity.RESULT_OK, Activity.RESULT_FIRST_USER, Activity.RESULT_CANCELED])
@Retention(AnnotationRetention.SOURCE)
internal annotation class FragmentResultCode

