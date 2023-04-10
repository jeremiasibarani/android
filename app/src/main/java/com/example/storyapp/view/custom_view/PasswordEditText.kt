package com.example.storyapp.view.custom_view

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class PasswordEditText : AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.length < 8){
                    background = ContextCompat.getDrawable(context, R.drawable.edit_text_error_bg)
                    error = resources.getString(R.string.less_than_8_char_password_error)
                }else{
                    background = ContextCompat.getDrawable(context, R.drawable.edit_text_bg)
                }
            }
            override fun afterTextChanged(s: Editable) {

            }

        })
    }

    private fun init(){

    }

}