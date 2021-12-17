package fi.metropolia.climbstation.ui

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import fi.metropolia.climbstation.R

/**
 * Class for setting dropdown autoCompleteTextView
 * @param context Context for array adapter
 * @param textView AutoCompleteTextView
 * @param adapterList array list for the text view
 *
 * @author Minji Choi
 */
class DropDownList(context: Context, private val textView: AutoCompleteTextView, adapterList:List<String>) {

    init {
        val adapter = ArrayAdapter(context, R.layout.list_item, R.id.list_item,adapterList)
        textView.setAdapter(adapter)
        textView.setText(adapterList[0], false)
    }

    /**
     * set height of dropdown menu
     * @param height desired height
     */
    fun setDropDownHeight(height:Int){
        textView.dropDownHeight = height
    }
}