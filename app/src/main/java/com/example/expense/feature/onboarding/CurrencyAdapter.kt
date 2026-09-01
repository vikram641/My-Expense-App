package com.example.expense.feature.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expense.core.util.CurrencyConstants
import com.example.expense.databinding.ItemOnboardingCurrencyBinding

class CurrencyAdapter(
    private val currencyCodes: List<String>,
    private var selectedCode: String,
    private val onSelected: (String) -> Unit
) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    inner class CurrencyViewHolder(val binding: ItemOnboardingCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding = ItemOnboardingCurrencyBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val code = currencyCodes[position]
        val display = CurrencyConstants.getDisplayForCode(code)
        val name = display.substringAfter("- ", display)
        val isSelected = code == selectedCode

        holder.binding.tvSymbol.text = CurrencyConstants.getSymbol(code)
        holder.binding.tvName.text = name
        holder.binding.tvCode.text = code
        holder.binding.row.setBackgroundResource(
            if (isSelected) com.example.expense.R.drawable.bg_row_outline_selected
            else com.example.expense.R.drawable.bg_row_outline
        )
        holder.binding.ivCheck.visibility =
            if (isSelected) android.view.View.VISIBLE else android.view.View.INVISIBLE

        holder.binding.root.setOnClickListener {
            val previousCode = selectedCode
            selectedCode = code
            onSelected(code)
            notifyItemChanged(currencyCodes.indexOf(previousCode))
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = currencyCodes.size
}
