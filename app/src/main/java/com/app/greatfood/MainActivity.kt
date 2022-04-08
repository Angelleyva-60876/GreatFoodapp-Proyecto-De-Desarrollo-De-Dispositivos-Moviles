package com.app.greatfood

import android.app.Activity
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.app.greatfood.Model.ModelMain
import com.app.greatfood.ViewModel.MainAdapter
import com.app.greatfood.ViewModel.Api
import com.bumptech.glide.Priority
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class MainActivity : AppCompatActivity(), MainAdapter.onSelectData {

    var mainAdapter: MainAdapter? = null
    var modelMain: MutableList<ModelMain> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mLayoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        rvMainMenu.setLayoutManager(mLayoutManager)
        rvMainMenu.setHasFixedSize(true)
        categorias
    }

    private val categorias: Unit
        private get() {
            AndroidNetworking.get(Api.Categorias)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        try {
                            val playerArray = response.getJSONArray("categories")
                            for (i in 0 until playerArray.length()) {

                                val temp = playerArray.getJSONObject(i)
                                val dataApi = ModelMain()
                                dataApi.strCategory = temp.getString("strCategory")
                                dataApi.strCategoryThumb = temp.getString("strCategoryThumb")
                                dataApi.strCategoryDescription = temp.getString("strCategoryDescription")
                                modelMain.add(dataApi)
                                showCategories()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(this@MainActivity,
                                "Gagal menampilkan data!", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }

    private fun showCategories() {
        mainAdapter = MainAdapter(this@MainActivity, modelMain, this)
        MainMenu!!.adapter = mainAdapter
    }

    override fun onSelected(modelMain: ModelMain?) {
        TODO("Not yet implemented")
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