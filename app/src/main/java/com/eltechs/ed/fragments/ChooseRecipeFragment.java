package com.eltechs.ed.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.eltechs.ed.InstallRecipe;
import com.eltechs.ed.R;
import java.util.List;

public class ChooseRecipeFragment extends Fragment {
    /* access modifiers changed from: private */
    public OnRecipeSelectedListener mListener;
    private RecyclerView mRecyclerView;

    public interface OnRecipeSelectedListener {
        void onRecipeSelected(InstallRecipe installRecipe);
    }

    private class RecipeAdapter extends Adapter<RecipeAdapter.ViewHolder> {
        /* access modifiers changed from: private */
        public final List<InstallRecipe> mItems;

        public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
            public ImageView mImage;
            public InstallRecipe mItem;
            public TextView mText;
            public final View mView;

            public ViewHolder(View view) {
                super(view);
                this.mView = view;
                this.mImage = (ImageView) view.findViewById(R.id.image);
                this.mText = (TextView) view.findViewById(R.id.text);
            }
        }

        public RecipeAdapter(List<InstallRecipe> list) {
            this.mItems = list;
        }

        public final ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.basic_list_item, viewGroup, false));
        }

        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            viewHolder.mItem = (InstallRecipe) this.mItems.get(i);
            if (i == getItemCount() - 1) {
                viewHolder.mImage.setImageResource(R.drawable.ic_help_24dp);
            } else {
                viewHolder.mImage.setImageResource(R.drawable.ic_description_24dp);
            }
            viewHolder.mText.setText(viewHolder.mItem.toString());
            viewHolder.mView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ChooseRecipeFragment.this.mListener.onRecipeSelected((InstallRecipe) RecipeAdapter.this.mItems.get(viewHolder.getAdapterPosition()));
                }
            });
        }

        public final int getItemCount() {
            return this.mItems.size();
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (OnRecipeSelectedListener) context;
        } catch (ClassCastException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append(context.toString());
            sb.append(" must implement OnRecipeSelectedListener");
            throw new ClassCastException(sb.toString());
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        FrameLayout frameLayout = (FrameLayout) layoutInflater.inflate(R.layout.basic_list, viewGroup, false);
        this.mRecyclerView = (RecyclerView) frameLayout.findViewById(R.id.list);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mRecyclerView.getContext()));
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(this.mRecyclerView.getContext(), 1));
        return frameLayout;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mRecyclerView.setAdapter(new RecipeAdapter(InstallRecipe.LIST));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((int) R.string.wd_title_select_install_recipe);
    }
}
