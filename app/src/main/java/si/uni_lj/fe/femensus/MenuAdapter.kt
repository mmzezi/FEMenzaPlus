package si.uni_lj.fe.femensus

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(private val context: Context, private val menuList: List<MenuItem>) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menuItem = menuList[position]
        holder.meniTitle.text = menuItem.title
        
        val lines = menuItem.details.split("\n").filter { it.isNotBlank() }
        
        val contentText = when {
            menuItem.type.contains("enolončnica", ignoreCase = true) -> {
                lines.take(2).joinToString("\n") { "● $it" }
            }
            lines.size >= 3 -> {
                "● ${lines[1]}\n● ${lines[2]}"
            }
            else -> {
                lines.joinToString("\n") { "● $it" }
            }
        }
        
        holder.meniContent.text = contentText
        holder.meniIcon.setImageResource(getIconResId(menuItem.type))

        holder.itemView.setOnClickListener {
            showMenuDetailsDialog(menuItem)
        }
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    private fun showMenuDetailsDialog(menuItem: MenuItem) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_menu_details)
        
        // Set the dialog's window background to transparent to show the rounded corners correctly
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val meniIcon: ImageView = dialog.findViewById(R.id.meni_icon)
        val meniTitle: TextView = dialog.findViewById(R.id.meni_title)
        val meniType: TextView = dialog.findViewById(R.id.meni_type)
        val meniContent: TextView = dialog.findViewById(R.id.meni_content)
        val closeButton: ImageView = dialog.findViewById(R.id.close_button)

        meniIcon.setImageResource(getIconResId(menuItem.type))
        meniTitle.text = menuItem.title
        meniType.text = menuItem.type.replaceFirstChar { it.uppercase() }
        meniContent.text = menuItem.details

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getIconResId(type: String): Int {
        return when {
            type.contains("perutnina", ignoreCase = true) -> R.drawable.perutnina
            type.contains("mleto meso", ignoreCase = true) -> R.drawable.meat_grinder_new
            type.contains("rdeče meso", ignoreCase = true) -> R.drawable.rdece_meso
            type.contains("vege", ignoreCase = true) -> R.drawable.vege
            type.contains("testenine", ignoreCase = true) -> R.drawable.testenine
            type.contains("solata", ignoreCase = true) -> R.drawable.solata
            type.contains("enolončnica", ignoreCase = true) -> R.drawable.enoloncnica
            type.contains("riba", ignoreCase = true) || type.contains("ribe", ignoreCase = true) -> R.drawable.riba
            else -> R.drawable.icon_final_1_ // default icon
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val meniTitle: TextView = itemView.findViewById(R.id.meni_title)
        val meniContent: TextView = itemView.findViewById(R.id.meni_content)
        val meniIcon: ImageView = itemView.findViewById(R.id.meni_type)
    }
}
