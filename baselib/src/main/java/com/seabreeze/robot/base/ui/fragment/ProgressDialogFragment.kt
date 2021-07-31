package com.seabreeze.robot.base.ui.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.seabreeze.robot.base.R
import de.hdodenhof.circleimageview.CircleImageView

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/11
 * @description : 等待框 loading ...
 * </pre>
 */
class ProgressDialogFragment : DialogFragment() {

    companion object {
        fun newInstance() = ProgressDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Dialog_FullScreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_progress_dialog, container, false)
    }

    private var mMessage: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvMessage = view.findViewById<TextView>(R.id.tvMessage)
        tvMessage.text = mMessage ?: getString(R.string.loading)
    }

    override fun onStart() {
        super.onStart()
        dialog!!.apply {
            window!!.apply {
                val windowParams = attributes
                windowParams.dimAmount = 0.0f //将Window周边设置透明为0.7
                setCanceledOnTouchOutside(false) //点击周边不隐藏对话框
                attributes = windowParams
                setGravity(Gravity.TOP)
            }
        }
    }

    fun show(
        fragmentManager: FragmentManager,
        message: String?,
        isCancelable: Boolean = false
    ) {
        this.mMessage = message
        this.isCancelable = isCancelable
        try {
            super.show(fragmentManager, "progressDialogFragment")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}