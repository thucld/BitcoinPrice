package com.thucld.bitcoin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    // We will use the Coin Desk API to get Bitcoin price
    private val url = "https://api.coindesk.com/v1/bpi/currentprice.json"
    var okHttpClient: OkHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.load -> {
                loadBitcoinPrice()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun loadBitcoinPrice() {
        progressBar.visibility = View.VISIBLE

        val request: Request = Request.Builder().url(url).build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {}

            override fun onResponse(call: Call?, response: Response?) {
                val json = response?.body()?.string()
                // we get the json response returned by the Coin Desk API
                // make this call on a browser for example to watch the properties
                // here we get USD and EUR rates properties
                // we split the value got just to keep the integer part of the values
                val usdRate = (JSONObject(json).getJSONObject("bpi").getJSONObject("USD")["rate"] as String).split(".")[0]
                val eurRate = (JSONObject(json).getJSONObject("bpi").getJSONObject("EUR")["rate"] as String).split(".")[0]

                runOnUiThread {
                    progressBar.visibility = View.GONE
                    tvCoinValue.text = "$$usdRate\n\n$eurRate€"
                }
            }
        })
    }

}
