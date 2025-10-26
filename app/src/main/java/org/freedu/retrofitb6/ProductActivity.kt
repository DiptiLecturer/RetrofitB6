package org.freedu.retrofitb6

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import org.freedu.retrofitb6.Model.Product
import org.freedu.retrofitb6.adapter.ProductAdapter
import org.freedu.retrofitb6.databinding.ActivityProductBinding
import org.freedu.retrofitb6.netwrok.ApiClient
import org.freedu.retrofitb6.utils.NetworkUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductBinding
    private val productList = ArrayList<Product>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.overScrollMode = View.OVER_SCROLL_NEVER

        binding.btnRetry.setOnClickListener {
            fetchProducts()
        }

        fetchProducts()
    }
    @SuppressLint("SetTextI18n")
    private fun fetchProducts() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            binding.tvError.text = "No Internet Connection!"
            binding.btnRetry.visibility = View.VISIBLE
            return
        }
        binding.progressBar.visibility = View.VISIBLE
        binding.tvError.visibility = View.GONE

        ApiClient.instance.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>?>,
                response: Response<List<Product>?>
            ) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    productList.clear()
                    productList.addAll(response.body()!!)
                    binding.recyclerView.adapter = ProductAdapter(productList)
                } else {
                    binding.tvError.text = "Error loading products!"
                    binding.tvError.visibility = View.VISIBLE
                    binding.btnRetry.visibility = View.VISIBLE
                }
            }

            override fun onFailure(
                call: Call<List<Product>?>,
                t: Throwable
            ) {
                binding.progressBar.visibility = View.GONE
                binding.tvError.text = "Failed: ${t.message}"
                binding.tvError.visibility = View.VISIBLE
                binding.btnRetry.visibility = View.VISIBLE
            }
        })
    }
}