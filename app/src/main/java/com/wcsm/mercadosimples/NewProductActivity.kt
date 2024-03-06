package com.wcsm.mercadosimples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import com.wcsm.mercadosimples.database.ProductDAO
import com.wcsm.mercadosimples.databinding.ActivityNewProductBinding
import com.wcsm.mercadosimples.model.Product
import java.util.UUID
import kotlin.random.Random

class NewProductActivity : AppCompatActivity() {

    private val binding by lazy {ActivityNewProductBinding.inflate(layoutInflater)}

    private lateinit var productDAO: ProductDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        productDAO = ProductDAO(this)

        binding.btnAddProduct.setOnClickListener {
            val id = UUID.randomUUID().toString()
            val name = binding.editName.text.toString()
            val quantity = binding.editQuantity.text.toString().toInt()
            val price = binding.editPrice.text.toString().toDouble()

            val newProduct = Product(id, name, quantity, price)
            addProduct(newProduct)
            finish()
        }
    }

    private fun addProduct(newProduct: Product) {
        productDAO.save(newProduct)
    }
}