package adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ever.four.deptomaniger.instant.R
import entity.ItemList


class ItemRecyclerDataAdapter: RecyclerView.Adapter<ItemRecyclerDataAdapter.ItemViewHolder> {
    var itemLists: MutableList<ItemList>

    class ItemViewHolder: RecyclerView.ViewHolder {
        var name: TextView
        var owner: TextView
        var description: TextView
        var amount: TextView
        var currency: TextView

        constructor(view: View): super(view) {
            name = view.findViewById(R.id.name)
            owner = view.findViewById(R.id.owner)
            description = view.findViewById(R.id.description)
            amount = view.findViewById(R.id.amount)
            currency = view.findViewById(R.id.currency)
        }
    }

    constructor(itemLists: MutableList<ItemList>) {
        this.itemLists = itemLists
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card, parent, false))
    }

    override fun getItemCount(): Int {
        return itemLists.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var player = itemLists.get(position)
        holder.name.setText(player.name)
        holder.owner.setText(player.owner)
        holder.description.setText(player.description)
        holder.amount.setText(player.amount.toString())
        holder.currency.setText(player.currency)
    }
}
