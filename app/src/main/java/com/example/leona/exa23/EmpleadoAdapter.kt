package com.example.leona.exa23

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.leona.exa23.database.EmpleadoEntry
import kotlinx.android.synthetic.main.empleado_list_item.view.*

class    EmpleadoAdapter (private  var mEmpleadoEntries:List<EmpleadoEntry>, private val mContext: Context, private val clikckListener:(EmpleadoEntry)-> Unit)
    : RecyclerView.Adapter<EmpleadoAdapter.EmpleadoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):EmpleadoViewHolder{
        val layoutInflater= LayoutInflater.from(mContext)
        return EmpleadoViewHolder(layoutInflater.inflate(R.layout.empleado_list_item,parent,false))
    }


    override fun onBindViewHolder(holder: EmpleadoViewHolder, position: Int) {
        holder.bind(mEmpleadoEntries[position], mContext, clikckListener)
    }


    override fun getItemCount(): Int = mEmpleadoEntries.size


    fun setEmpleado(contactoEntries: List<EmpleadoEntry>){
        mEmpleadoEntries = contactoEntries
        notifyDataSetChanged()
    }

    fun getTasks(): List<EmpleadoEntry> = mEmpleadoEntries


    class EmpleadoViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind (contact:EmpleadoEntry, context: Context, clickListener: (EmpleadoEntry) -> Unit){
            //Asigna los valores a los elementos delcontacto_list_item
            itemView.tvNombre.text = contact.Nombre
            itemView.tvTelefono.text = contact.Domicilio

            itemView.setOnClickListener{ clickListener(contact)}
        }
    }

}

