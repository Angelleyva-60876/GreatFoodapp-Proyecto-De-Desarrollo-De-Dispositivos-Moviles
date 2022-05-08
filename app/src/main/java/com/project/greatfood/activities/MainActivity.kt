package com.project.greatfood.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.project.greatfood.R
import com.project.greatfood.adapter.MainAdapter
import com.project.greatfood.model.ModelMain
import com.project.greatfood.networking.Api
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_main.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import android.widget.ArrayAdapter
import android.widget.ListView

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), MainAdapter.onSelectData {

    var mainAdapter: MainAdapter? = null
    var mainActivity: MainActivity? = null
    var progressDialog: ProgressDialog? = null
    var modelMain: MutableList<ModelMain> = ArrayList()
    lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }

        //tddd
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }

        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Porfavor espere")
        progressDialog!!.setMessage("Cargando datos ...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)

        val mLayoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        rvMainMenu.setLayoutManager(mLayoutManager)
        rvMainMenu.setHasFixedSize(true)

        //Methods get data
        categories

        searchView()
        openGithub()
    }

    private val categories: Unit
        private get() {
            progressDialog!!.show()
            AndroidNetworking.get(Api.Categories)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        try {
                            progressDialog!!.dismiss()
                            val playerArray = response.getJSONArray("categories")
                            for (i in 0 until playerArray.length()) {

                                val temp = playerArray.getJSONObject(i)
                                val dataApi = ModelMain()
                                dataApi.strCategory = temp.getString("strCategory")
                                dataApi.strCategoryThumb = temp.getString("strCategoryThumb")
                                dataApi.strCategoryDescription =
                                    temp.getString("strCategoryDescription")
                                modelMain.add(dataApi)
                                showCategories()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@MainActivity,
                                "Gagal menampilkan data!", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onError(anError: ANError) {
                        progressDialog!!.dismiss()
                        Toast.makeText(
                            this@MainActivity,
                            "No hay conexion a internet!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

    private fun showCategories() {
        mainAdapter = MainAdapter(this@MainActivity, modelMain, this)
        rvMainMenu!!.adapter = mainAdapter
    }

    override fun onSelected(modelMain: ModelMain) {
        val intent = Intent(this@MainActivity, FilterFoodActivity::class.java)
        intent.putExtra("showFilter", modelMain)
        startActivity(intent)
    }

    // Aqui agregamos la funcion para que al momento de dar click en la imagen nos mande
    // a nuestro repositorio
    private fun openGithub() {
        imgOpenGithub!!.setOnClickListener {
            val uri =
                Uri.parse("https://github.com/Angelleyva-60876/GreatFoodapp-Proyecto-De-Desarrollo-De-Dispositivos-Moviles/tree/Carlos-Aguilar")
            startActivity(Intent.createChooser(Intent(Intent.ACTION_VIEW, uri), "Open with"))
        }
    }

    fun getItemCount(): Int {
        return modelMain.size
    }

   //
    private fun searchView() {
        searchView = findViewById(R.id.search_recipe)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                setSearchView(query)
                return false
            }
                /*if (modelMain.contains(query)) {
                    mainAdapter.filter.filter(query)
                } else {
                    Toast.makeText(this@MainActivity, "No Match found", Toast.LENGTH_LONG).show()
                }
                return false
            }*/

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText == "") {
                    getItemCount()
                }
                return false
            }
        })
    }

    private fun setSearchView(query: String) {
            Toast.makeText(this@MainActivity, "Casi lista", Toast.LENGTH_LONG).show()
        if (modelMain.contains(query)) {
            //modelMain.add(dataApi)
            mainAdapter.filter().filter(query);
        } else {
            Toast.makeText(this@MainActivity, "No se encuentra", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        //Set Transparent Status bar
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val win = activity.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }
    }
}