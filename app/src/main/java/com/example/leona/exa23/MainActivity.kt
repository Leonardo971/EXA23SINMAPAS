package com.example.leona.exa23

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.leona.exa23.database.AppDatabase
import com.example.leona.exa23.database.EmpleadoEntry
import com.example.leona.exa23.helper.doAsync
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {

    var hilo: ObtenerWebService? = null
    var nomEmpleado : String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun btnReg(v: View){
        var Cor: String =correo.text.toString()
        var con: String =etNIP.text.toString()
        var tel: String= "412123042"
        hilo = ObtenerWebService()
        hilo?.execute("NoJala", "0", Cor, con, tel)
        //Toast.makeText(baseContext,"Valores " +Cor+con+tel, Toast.LENGTH_LONG).show()

    }

    //-----------------------Servicio Web de Registro--------------------------
    inner class ObtenerWebService() : AsyncTask<String, String, String>() {
        var correo : String = ""

        override fun doInBackground(vararg params: String?): String {
            var url: URL? = null
            var devuelve = ""
            try {
                val urlConn: HttpURLConnection
                val printout: DataOutputStream
                val input: DataInputStream
                //modificar la dirrecion ip de acuerdo ala red
                url = URL("http://192.168.1.67/proyecto/RegEmpleado.php")

                //  Abrimos la conexión hacia el servicio web alojado en el servidor
                urlConn = url.openConnection() as HttpURLConnection
                urlConn.doInput = true
                urlConn.doOutput = true
                urlConn.useCaches = false
                urlConn.setRequestProperty("Content-Type", "application/json")
                urlConn.setRequestProperty("Accept", "application/json")
                urlConn.connect()
                // Creando parametros que vamos a enviar
                val jsonParam = JSONObject()
                /* Encrypt
                val encrypted = MCrypt.bytesToHex(mcrypt.encrypt("Text to Encrypt")) */

                //val cor =  MCrypt.bytesToHex(mcrypt.encrypt(params[2]));
                //val co =MCrypt.bytesToHex(mcrypt.encrypt(params[3]));
                //val te = MCrypt.bytesToHex(mcrypt.encrypt(params[4]))
                val cor =(params[2])
                val co =(params[3])
                val te= (params[4])
                Log.d("Barcenas",params[2] + " = " + cor)
                correo = params[2].toString()
                jsonParam.put("Cor",cor)
                jsonParam.put("Nip", co)
                jsonParam.put("Tlf", te)

                //Envio de parametros por el método post
                val os = urlConn.outputStream
                val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                // Escribe los datos a través de los métodos flush() y write()
                Log.d("BARCENAS1", jsonParam.toString())
                writer.write(jsonParam.toString())
                writer.flush()
                writer.close()
                val respuesta = urlConn.responseCode

                val result = StringBuilder()
                // Preguntamos si se pudo conectar al servidor con exito
                if (respuesta == HttpURLConnection.HTTP_OK) {
                    // El siguiente proceso de hace por que JSONObject necesita un string para
                    // concatenar lo que envio el servicio web de regreso qu es un JSON
                    val inStream: InputStream = urlConn.inputStream
                    val isReader = InputStreamReader(inStream)
                    val bReader = BufferedReader(isReader)
                    var tempStr: String?
                    while (true) {
                        tempStr = bReader.readLine()
                        if (tempStr == null) {
                            break
                        }
                        result.append(tempStr)
                    }
                    urlConn.disconnect()
                    devuelve = result.toString() // Regresa un JSON al método onPostExecute
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return devuelve
        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            var cla :  String
            var nom: String
            var apell: String
            var edad: String
            var dir : String
            var nomre : String
            var monayu : String
            val devuelve = ""
            var suc: String
            var msg: String

            //mandamos ala siguiente actividad
            //mandamos ala siguiente actividad
            val inte=Intent(this@MainActivity,MainActivityLista::class.java)


            Log.d("Barcenas",s)
            try {
                val respuestaJSON = JSONObject(s.toString())
                val resultJSON = respuestaJSON.getString("success") //Obtiene el primer campo de JSON que es string y se llama estado
                val msgJSON = respuestaJSON.getString("message")
                //nomEmpleado =  desencripta(String(mcrypt.decrypt(respuestaJSON.getString("nomempleado"))))
                nomEmpleado =  respuestaJSON.getString("nomempleado")
                val visitasJSON = respuestaJSON.getJSONArray("visitas")
                Log.d("Barcenas",nomEmpleado)
                when (resultJSON) {
                    "200"   // Se inserto correctamente uno nuevo
                    -> {
                        // Insert a sqlite alumno(nocontrol,nomalumno,nip)

                        // Recorrer el arreglo de avisos y insertarlo en sqlite
                        if (visitasJSON.length() >= 1) {
                            for (i in 0 until visitasJSON.length()) {

                                cla=(visitasJSON.getJSONObject(i).getString("clave"))
                                nom=(visitasJSON.getJSONObject(i).getString("nombre"))
                                apell=(visitasJSON.getJSONObject(i).getString("apellido"))
                                edad=(visitasJSON.getJSONObject(i).getString("edad"))
                                dir=(visitasJSON.getJSONObject(i).getString("direccion"))
                                nomre=(visitasJSON.getJSONObject(i).getString("nomresponsable"))
                                monayu=(visitasJSON.getJSONObject(i).getString("montoayuda"))
                                //Log.d("Barcenas",nom) //<------------Insert

                                val em: EmpleadoEntry =EmpleadoEntry(id =cla.toLong() ,Nombre = nom,Apellido = apell,edad =edad.toFloat() ,Domicilio =dir ,nomencargado =nomre ,Ayuda =monayu.toFloat())
                                doAsync{
                                    AppDatabase.getInstance(this@MainActivity)!!.empleadoDao().insertEmpleado(em)
                                    runOnUiThread {

                                    }
                                }.execute()

                            }
                        } else {
                            Toast.makeText(baseContext,"No hay visitas", Toast.LENGTH_LONG).show()
                        }
                        Toast.makeText(baseContext,"Success 200: " + msgJSON, Toast.LENGTH_LONG).show()
                        //Inicializamos la siguiente Actividad
                        //Inicializamos la siguiente Actividad
                        startActivity(inte)


                    }
                    "422"  // Falta Información en el web service
                    -> {
                        val messageJSON4 = respuestaJSON.getString("message")
                        Toast.makeText(baseContext,"Error 422: " + messageJSON4, Toast.LENGTH_LONG).show()
                    }
                    "500"  // Error al insertar el registro
                    -> {
                        val messageJSON3 = respuestaJSON.getString("message")
                        Toast.makeText(baseContext, "Error 500: " +messageJSON3, Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: JSONException) {
                Toast.makeText(baseContext, "Error:" + e.toString(), Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    } // fin del web servic
}
