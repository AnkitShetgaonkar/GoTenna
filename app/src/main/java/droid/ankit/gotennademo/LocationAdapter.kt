package droid.ankit.gotennademo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import droid.ankit.database.PinPoint
import kotlinx.android.synthetic.main.list_item.view.*

class LocationAdapter(private val context: Context)
    :RecyclerView.Adapter<ViewHolder>() {

    private var items:List<PinPoint> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(list:List<PinPoint>) {
        this.items = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = items[position].name
        holder.tvDescrip.text = items[position].description
    }


}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val tvName = view.tvName!!
    val tvDescrip = view.tvDescription!!
}