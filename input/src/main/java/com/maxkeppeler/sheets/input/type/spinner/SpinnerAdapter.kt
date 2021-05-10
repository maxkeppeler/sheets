package com.maxkeppeler.sheets.input.type.spinner

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.maxkeppeler.sheets.input.R
import java.lang.ClassCastException
import java.lang.IllegalStateException
import java.lang.RuntimeException
import java.util.*

@Suppress("unused")
open class SpinnerAdapter(
    mContext: Context,
    private var mObjects: MutableList<SpinnerOption> = mutableListOf(),
    private var mObjectsFromResources: Boolean = false,
    private var mNotifyOnChange: Boolean = true
) : BaseAdapter(), Filterable {

    private val mLock = Any()
    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)

    // A copy of the original mObjects array, initialized from and then used instead as soon as
    // the mFilter ArrayFilter is used. mObjects will then only contain the filtered values.
    private var mOriginalValues: ArrayList<SpinnerOption>? = null
    private var mFilter: ArrayFilter? = null

    /** Layout inflater used for [.getDropDownView].  */
    private var mDropDownInflater: LayoutInflater? = null

    fun add(o: SpinnerOption) {
        synchronized(mLock) {
            if (mOriginalValues != null) {
                mOriginalValues!!.add(o)
            } else {
                mObjects.add(o)
            }
            mObjectsFromResources = false
        }
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    fun addAll(collection: Collection<SpinnerOption>) {
        synchronized(mLock) {
            if (mOriginalValues != null) {
                mOriginalValues!!.addAll(collection)
            } else {
                mObjects.addAll(collection)
            }
            mObjectsFromResources = false
        }
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    fun insert(o: SpinnerOption, index: Int) {
        synchronized(mLock) {
            if (mOriginalValues != null) {
                mOriginalValues!!.add(index, o)
            } else {
                mObjects.add(index, o)
            }
            mObjectsFromResources = false
        }
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    fun remove(o: SpinnerOption) {
        synchronized(mLock) {
            if (mOriginalValues != null) {
                mOriginalValues!!.remove(o)
            } else {
                mObjects.remove(o)
            }
            mObjectsFromResources = false
        }
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    fun clear() {
        synchronized(mLock) {
            if (mOriginalValues != null) {
                mOriginalValues!!.clear()
            } else {
                mObjects.clear()
            }
            mObjectsFromResources = false
        }
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    fun sort(comparator: Comparator<SpinnerOption>) {
        synchronized(mLock) {
            if (mOriginalValues != null) {
                Collections.sort(mOriginalValues, comparator)
            } else {
                Collections.sort(mObjects, comparator)
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
        mNotifyOnChange = true
    }

    fun setNotifyOnChange(notifyOnChange: Boolean) {
        mNotifyOnChange = notifyOnChange
    }

    override fun getCount(): Int {
        return mObjects.size
    }

    override fun getItem(position: Int): SpinnerOption {
        return mObjects[position]
    }

    fun getPosition(item: SpinnerOption): Int {
        return mObjects.indexOf(item)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(
        position: Int, convertView: View?,
        parent: ViewGroup
    ): View {
        return createViewFromResource(mInflater, position, convertView, parent, R.layout.spinner_item)
    }

    private fun createViewFromResource(
        inflater: LayoutInflater, position: Int,
        convertView: View?, parent: ViewGroup, resource: Int
    ): View {
        val text: TextView?
        val icon: ImageView?
        val view: View = convertView ?: inflater.inflate(resource, parent, false)
        try {
            text = view.findViewById(R.id.displayText)
            icon = view.findViewById(R.id.icon)
            if (text == null || icon == null)
                throw RuntimeException("Failed to find view in item layout")
        } catch (e: ClassCastException) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView")
            throw IllegalStateException(
                "ArrayAdapter requires the resource ID to be a TextView", e
            )
        }
        getItem(position).let {
            text.text = it.displayText
            it.getDrawable(view.context).let { drawable ->
                if (drawable != null) {
                    icon.setImageDrawable(drawable)
                    icon.visibility = View.VISIBLE
                } else {
                    icon.visibility = View.GONE
                }
            }
        }
        return view
    }

    override fun getDropDownView(
        position: Int, convertView: View?,
        parent: ViewGroup
    ): View {
        return createViewFromResource(
            (if (mDropDownInflater == null) mInflater else mDropDownInflater)!!,
            position,
            convertView,
            parent,
            R.layout.spinner_item
        )
    }

    override fun getFilter(): Filter {
        if (mFilter == null) {
            mFilter = ArrayFilter()
        }
        return mFilter!!
    }

    /**
     *
     * An array filter constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.
     */
    inner class ArrayFilter : Filter() {

        override fun performFiltering(prefix: CharSequence): FilterResults {
            val results = FilterResults()
            if (mOriginalValues == null) {
                synchronized(mLock) {
                    Collections.copy(mOriginalValues, mObjects)
                }
            }
            if (prefix.isEmpty()) {
                val list = arrayListOf<SpinnerOption>()
                synchronized(mLock) { Collections.copy(list, mOriginalValues) }
                results.values = list
                results.count = list.size
            } else {
                val prefixString = prefix.toString().toLowerCase(Locale.ROOT)
                val values = arrayListOf<SpinnerOption>()
                synchronized(mLock) { Collections.copy(values, mOriginalValues) }
                val newValues = arrayListOf<SpinnerOption>()

                values.forEach { value ->
                    val valueText: String = value.displayText.toLowerCase(Locale.ROOT)

                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value)
                    } else {
                        valueText.split(" ").toTypedArray()
                            .firstOrNull { it.startsWith(prefixString) }?.let {
                            newValues.add(value)
                        }
                    }
                }
                results.values = newValues
                results.count = newValues.size
            }
            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            mObjects = results.values as MutableList<SpinnerOption>
            if (results.count > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }
}