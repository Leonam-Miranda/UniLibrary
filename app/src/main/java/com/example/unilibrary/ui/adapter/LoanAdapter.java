package com.example.unilibrary.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unilibrary.R;
import com.example.unilibrary.model.LoanWithBook;

import java.util.ArrayList;
import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.LoanViewHolder> {

    private List<LoanWithBook> loans = new ArrayList<>();
    private final OnLoanClickListener listener;

    public interface OnLoanClickListener {
        void onRenewClick(LoanWithBook loan);
    }

    public LoanAdapter(OnLoanClickListener listener) {
        this.listener = listener;
    }

    public void setLoans(List<LoanWithBook> loans) {
        this.loans = loans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_loan, parent, false);
        return new LoanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanViewHolder holder, int position) {
        holder.bind(loans.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return loans.size();
    }

    static class LoanViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle, tvAuthor, tvDueDate;
        private final ImageView ivCover;
        private final View btnRenew;

        public LoanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvBookTitle);
            tvAuthor = itemView.findViewById(R.id.tvBookAuthor);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            ivCover = itemView.findViewById(R.id.ivBookCover);
            btnRenew = itemView.findViewById(R.id.btnRenew);
        }

        public void bind(LoanWithBook loanWithBook, OnLoanClickListener listener) {
            tvTitle.setText(loanWithBook.book.getTitle());
            tvAuthor.setText(loanWithBook.book.getAuthor());

            // Carregar imagem
            if (loanWithBook.book.getCoverResId() != 0) {
                ivCover.setImageResource(loanWithBook.book.getCoverResId());
            } else {
                ivCover.setImageResource(R.color.background_light_grey);
            }

            long diff = loanWithBook.loan.getDueDate().getTime() - System.currentTimeMillis();
            long days = diff / (24L * 60 * 60 * 1000);
            
            if (days < 0) {
                tvDueDate.setText("Atrasado!");
                tvDueDate.setTextColor(itemView.getContext().getColor(R.color.primary_red));
            } else {
                tvDueDate.setText("Vence em " + days + " dias");
                tvDueDate.setTextColor(itemView.getContext().getColor(R.color.text_secondary));
            }

            btnRenew.setOnClickListener(v -> listener.onRenewClick(loanWithBook));
        }
    }
}
