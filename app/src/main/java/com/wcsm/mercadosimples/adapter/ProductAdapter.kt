package com.wcsm.mercadosimples.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wcsm.mercadosimples.databinding.ProductBinding
import com.wcsm.mercadosimples.model.Product
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private val context: Context,
    private val totalTextView: TextView,
    val onClickDelete: (String) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var productsList: MutableList<Product> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun addList(list: MutableList<Product>) {
        this.productsList = list
        updateTotalPrice()
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(itemBinding: ProductBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
            private val binding: ProductBinding

            init {
                binding = itemBinding
            }

            fun bind(product: Product) {
                with(binding) {
                    textName.text = product.name
                    textQuantity.text = product.quantity.toString()
                    textPrice.text = formatMoney(product.price)
                    textTotal.text = formatMoney(product.quantity * product.price)
                }

                itemView.setOnClickListener {
                    deleteProductAlertDialog(product)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val productBinding = ProductBinding.inflate(layoutInflater, parent, false)
        return ProductViewHolder(productBinding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productsList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotalPrice() {
        var total = 0.0
        productsList.forEach {
            val totalPerProduct = it.price * it.quantity
            total += totalPerProduct
        }
        totalTextView.text = "Valor Total: ${formatMoney(total)}"
    }

    private fun deleteProductAlertDialog(product: Product) {
        AlertDialog.Builder(context)
            .setTitle("Deletar Produto")
            .setMessage("Tem certeza que deseja deletar o produto ${product.name} ?")
            .setPositiveButton("DELETAR") {dialog, _ ->
                onClickDelete(product.id)
                dialog.dismiss()

            }
            .setNegativeButton("CANCELAR") {dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun formatMoney(value: Double) : String {
        val valueFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return valueFormat.format(value)
    }
}