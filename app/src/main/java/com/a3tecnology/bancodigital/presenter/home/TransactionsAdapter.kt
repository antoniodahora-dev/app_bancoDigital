package com.a3tecnology.bancodigital.presenter.home

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.a3tecnology.bancodigital.R
import com.a3tecnology.bancodigital.data.enum.TransactionOperation
import com.a3tecnology.bancodigital.data.enum.TransactionType
import com.a3tecnology.bancodigital.data.model.Transaction
import com.a3tecnology.bancodigital.databinding.TransactionItemBinding
import com.a3tecnology.bancodigital.util.GetMask

class TransactionsAdapter(
    private val context: Context,
    private val transactionSelected: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionsAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(
                oldItem: Transaction,
                newItem:Transaction
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Transaction,
                newItem: Transaction
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionsAdapter.ViewHolder {
      return ViewHolder(TransactionItemBinding.inflate(
           LayoutInflater.from(parent.context),
           parent,
           false
            )
       )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = getItem(position)

        transaction.operation?.let {
            holder.binding.transitionDescription.text = TransactionOperation.getOperation(it)

            holder.binding.txtTransactionType.text = TransactionType.getType(it).toString()
            holder.binding.txtTransactionType.backgroundTintList =
                if (transaction.type == TransactionType.CASH_IN) {
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_cashIn))
                } else {
                    ColorStateList.valueOf(ContextCompat.getColor(context,R.color.color_cashOut))
                }
        }


        holder.binding.txtTransitionValue.text = context.getString(
            R.string.txt_formated_value, GetMask.getFormatedValue(transaction.amount)
        )
        holder.binding.txtTransitionDate.text =
            GetMask.getFormatedDate(transaction.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)

        holder.itemView.setOnClickListener{
            transactionSelected(transaction)
        }
    }

    inner class ViewHolder(val binding: TransactionItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}