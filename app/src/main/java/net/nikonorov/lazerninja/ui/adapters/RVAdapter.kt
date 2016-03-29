package net.nikonorov.lazerninja.ui.adapters

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import net.nikonorov.lazerninja.App
import net.nikonorov.lazerninja.R
import net.nikonorov.lazerninja.ui.ActivityBluetooth
import java.util.*
import java.util.zip.Inflater

/**
 * Created by vitaly on 23.03.16.
 */
class RVAdapter(val context : Context, val data : ArrayList<BluetoothDevice>) : RecyclerView.Adapter<RVAdapter.CardViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder? {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent, false)
        return CardViewHolder(view).listen { pos, type ->
            val item = data.get(pos)
            ((context as ActivityBluetooth).application as App).device = item
            Log.i("HOLDERLOG", item.name)
        }
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        if(data.get(position).name != null) {
            holder.nameTV?.text = data.get(position).name
        }else{
            holder.nameTV?.text = "unnamed $position"
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var nameTV : TextView? = null

        init {
            nameTV = itemView.findViewById(R.id.device_name) as TextView
        }

//        override fun onClick(v: View) {
//            Log.i("HOLDERLOG", nameTV?.text.toString())
//        }
    }

}


fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(getAdapterPosition(), getItemViewType())
    }
    return this
}