package fi.metropolia.climbstation.ui

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import fi.metropolia.climbstation.R

class DropDownList(context: Context, private val textView: AutoCompleteTextView, adapterList:List<String>) {

    init {
        val adapter = ArrayAdapter(context, R.layout.list_item, R.id.list_item,adapterList)
        textView.setAdapter(adapter)
        textView.setText(adapterList[0], false)
    }

    fun setDropDownHeight(height:Int){
        textView.dropDownHeight = height
    }
}