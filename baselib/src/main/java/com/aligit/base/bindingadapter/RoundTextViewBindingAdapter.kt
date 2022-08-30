package com.aligit.base.bindingadapter

import androidx.databinding.BindingAdapter
import com.aligit.base.R
import com.aligit.base.ext.tool.getResColor
import com.aligit.base.ext.tool.log
import com.aligit.base.widget.round.RoundTextView
import com.aligit.base.widget.round.TextViewSelectedChangeListener

@BindingAdapter(value = ["mRvSelBackgroundColor", "mRvUnSelBackgroundColor"], requireAll = true)
fun bind(v: RoundTextView, rvSelBackgroundColor: Int? = null, rvUnSelBackgroundColor: Int? = null) {
    val listener = TextViewSelectedChangeListener{ delegate,isSelected ->
        if (isSelected){
            rvSelBackgroundColor?.let {
                delegate.backgroundColor = it
            }
        }else{
            rvUnSelBackgroundColor?.let {
                v.delegate.backgroundColor = it
            }
        }
    }
    v.setSelChangeListener(listener)
}