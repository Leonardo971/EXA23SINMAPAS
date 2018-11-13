package com.example.leona.exa23

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.leona.exa23.database.AppDatabase
import com.example.leona.exa23.database.EmpleadoEntry
import com.example.leona.exa23.helper.doAsync
import kotlinx.android.synthetic.main.activity_main_lista.*

class MainActivityLista : AppCompatActivity() {

    private lateinit var viewAdapter:EmpleadoAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val empleadoList:List<EmpleadoEntry> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_lista)

        viewManager = LinearLayoutManager(this)
        viewAdapter = EmpleadoAdapter(empleadoList,this,{ empleado: EmpleadoEntry ->  onItemClickListener(empleado)})


        recyclerViewEmp.apply{
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(this@MainActivityLista, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onResume() {
        super.onResume()
        retrieveEmpleados()
    }


    private fun retrieveEmpleados(){
        doAsync{
            val empleados = AppDatabase.getInstance(this@MainActivityLista)?.empleadoDao()?.loadAllEmpleado()
            runOnUiThread{
                viewAdapter.setEmpleado(empleados!!)
            }
        }.execute()
    }


    private fun onItemClickListener(empleado:EmpleadoEntry){
        // Launch AddTaskActivity adding the itemId as an extra in the intent
        //val intent = Intent(this,MainActivityDetalle::class.java)
        //intent.putExtra(MainActivityDetalle.EXTRA_CONTACTO_ID, contacto.id)
        //startActivity(intent)
        //Toast.makeText(this, "Clicked item" + task.description, Toast.LENGTH_LONG).show()
    }
}
