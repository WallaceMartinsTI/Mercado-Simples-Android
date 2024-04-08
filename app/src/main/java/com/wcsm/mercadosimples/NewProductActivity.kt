package com.wcsm.mercadosimples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.wcsm.mercadosimples.database.ProductDAO
import com.wcsm.mercadosimples.databinding.ActivityNewProductBinding
import com.wcsm.mercadosimples.model.Product
import java.util.UUID
import kotlin.random.Random

class NewProductActivity : AppCompatActivity() {

    private val binding by lazy {ActivityNewProductBinding.inflate(layoutInflater)}

    private lateinit var productDAO: ProductDAO
    private lateinit var nameInputLayout: TextInputLayout
    private lateinit var quantityInputLayout: TextInputLayout
    private lateinit var priceInputLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inicializeLayoutFields()

        productDAO = ProductDAO(this)

        binding.btnAddProduct.setOnClickListener {
            val id = UUID.randomUUID().toString()

            val name = binding.nameEditText.text.toString()
            val nameValidation = validateName(name)

            val quantity = binding.quantityEditText.text.toString()
            val quantityValidation = validateQuantity(quantity)

            val price = binding.priceEditText.text.toString()
            val priceValidation = validatePrice(price)

            val validation = nameValidation && quantityValidation && priceValidation
            if(validation) {
                val productQuantity = quantity.toInt()
                val productPrice = price.toDouble()

                val newProduct = Product(id, name, productQuantity, productPrice)
                addProduct(newProduct)
                finish()
            }
        }
    }

    private fun inicializeLayoutFields() {
        nameInputLayout = binding.nameInputLayout
        quantityInputLayout = binding.quantityInputLayout
        priceInputLayout = binding.priceInputLayout
    }

    private fun validateName(productName: String): Boolean {
        nameInputLayout.error = null
        if(productName.isEmpty()) {
            nameInputLayout.error = "O nome do produto não pode estar vazio."
            return false
        } else if(productName.length <= 1) {
            nameInputLayout.error = "O nome do produto é muito curto."
            return false
        } else if(productName.length > 50) {
            nameInputLayout.error = "O nome do produto é muito grande."
            return false
        }
        return true
    }

    private fun validateQuantity(productQuantity: String): Boolean {
        quantityInputLayout.error = null
        try {
            val quantity = productQuantity.toInt()
            if(quantity == 0) {
                quantityInputLayout.error = "Quantidade deve ser maior que 0."
                return false
            }
        } catch (e: NumberFormatException) {
            quantityInputLayout.error = "Quantidade não reconhecida."
            e.printStackTrace()
            return false
        } catch (e: Exception) {
            quantityInputLayout.error = "Houve algum erro."
            e.printStackTrace()
            return false
        }

        return true
    }

    private fun validatePrice(productPrice: String): Boolean {
        priceInputLayout.error = null
        try {
            val price = productPrice.toDouble()
            if(price == 0.0) {
                priceInputLayout.error = "Preço deve ser maior que 0."
                return false
            }
        } catch (e: NumberFormatException) {
            priceInputLayout.error = "Preço não reconhecida."
            e.printStackTrace()
            return false
        } catch (e: Exception) {
            priceInputLayout.error = "Houve algum erro."
            e.printStackTrace()
            return false
        }

        return true
    }

    private fun addProduct(newProduct: Product) {
        productDAO.save(newProduct)
    }
}