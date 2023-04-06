package com.example.storyapp.view.custom_view

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.example.storyapp.R
import com.example.storyapp.util.isEmailValid

class EmailEditText : AppCompatEditText {

    private lateinit var errorMessageTextView : AppCompatTextView

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
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(!isEmailValid(s.toString().trim())){
                    background = ContextCompat.getDrawable(context, R.drawable.edit_text_error_bg)
                }else{
                    background = ContextCompat.getDrawable(context, R.drawable.edit_text_bg)
                }
            }
            override fun afterTextChanged(s: Editable) {

            }

        })
    }

    private fun init(){
        errorMessageTextView = AppCompatTextView(context)
        errorMessageTextView.apply {
            setTextColor(ContextCompat.getColor(context, R.color.md_theme_light_error))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            visibility = View.GONE
        }
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        
    }
}