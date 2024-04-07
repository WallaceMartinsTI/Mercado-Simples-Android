package com.wcsm.mercadosimples

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.wcsm.mercadosimples.adapter.ProductAdapter
import com.wcsm.mercadosimples.database.ProductDAO
import com.wcsm.mercadosimples.databinding.ActivityMainBinding
import com.wcsm.mercadosimples.model.Product
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    
    private val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}

    private var productAdapter: ProductAdapter? = null

    private var productsList = mutableListOf<Product>()

    private var isAppFirstInitialization = true

    private lateinit var productDAO: ProductDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        productDAO = ProductDAO(this)

        productAdapter = ProductAdapter(
            this,
            binding.tvTotal
        ) { productId -> deleteProduct(productId) }

        binding.rvProducts.adapter = productAdapter
        binding.rvProducts.layoutManager = LinearLayoutManager(this)

        binding.fabAddProduct.setOnClickListener {
            startActivity(Intent(this, NewProductActivity::class.java))
        }

        binding.btnClearAllProducts.setOnClickListener {
            if(productsList.isEmpty()) {
                Toast.makeText(this, "Não há produtos na lista.", Toast.LENGTH_SHORT).show()
            } else {
                confirmDeleteAllTasksDialog()
            }
        }

        isAppFirstInitialization = savedInstanceState == null
    }

    private fun getProducts() {
        productsList = productDAO.listProducts()
    }

    override fun onResume() {
        super.onResume()

        if(!isAppFirstInitialization) {
            val lastProductAdded = productsList.lastOrNull()
            lastProductAdded?.let {
                showSnackbar(binding.root, it.name, it.id)
            }
        }
        isAppFirstInitialization = false
    }

    private fun confirmDeleteAllTasksDialog() {
        AlertDialog.Builder(this)
            .setTitle("Deletar Todos Produto")
            .setMessage("Tem certeza que deseja deletar todos os produto?")
            .setPositiveButton("DELETAR") {dialog, _ ->
                deleteAllProducts()
                dialog.dismiss()
            }
            .setNegativeButton("CANCELAR") {dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showSnackbar(view: View, name: String, id: String) {
        val snackBar = Snackbar.make(view, "$name Adicionado!", Snackbar.LENGTH_SHORT)
        snackBar.setAction("Desfazer") {
            productDAO.delete(id)
            updateProductsList()
        }
        snackBar.show()
    }

    private fun deleteProduct(productId: String) {
        productDAO.delete(productId)
        updateProductsList()
    }

    private fun deleteAllProducts() {
        productDAO.deleteALlProducts()
        updateProductsList()
    }

    private fun updateProductsList() {
        getProducts()
        productAdapter?.addList(productsList)
    }

    override fun onStart() {
        super.onStart()
        updateProductsList()
    }
}