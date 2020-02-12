package com.eltechs.ed.fragments;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.support.v7.widget.RecyclerView.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.eltechs.ed.*;
import java.util.*;

import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View.OnClickListener;

public class AdvancedOptionsFragment extends Fragment {
    /* access modifiers changed from: private */
    public OnAdvanceSelectedListener mListener;
    private RecyclerView mRecyclerView;

    public interface OnAdvanceSelectedListener {
        void onAdvanceSelected(InstallRecipe installRecipe);
    }

    private class AdvancedAdapter extends Adapter<AdvancedAdapter.ViewHolder> {
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

        public AdvancedAdapter(List<InstallRecipe> list) {
            this.mItems = list;
        }

        public final ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.basic_list_item, viewGroup, false));
        }

        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            viewHolder.mItem = (InstallRecipe) this.mItems.get(i);
			viewHolder.mImage.setImageResource(R.drawable.ic_description_24dp);
            viewHolder.mText.setText(viewHolder.mItem.toString());
            viewHolder.mView.setOnClickListener(new OnClickListener() {
					public void onClick(View view) {
						AdvancedOptionsFragment.this.mListener.onAdvanceSelected((InstallRecipe) AdvancedAdapter.this.mItems.get(viewHolder.getAdapterPosition()));
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
            this.mListener = (OnAdvanceSelectedListener) context;
        } catch (ClassCastException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append(context.toString());
            sb.append(" must implement OnAdvanceSelectedListener");
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
        this.mRecyclerView.setAdapter(new AdvancedAdapter(generateAdvancedRecipes(getResources().getStringArray(R.array.list_advanced_options))));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((int) R.string.wd_title_advanced_options);
    }
	
	private List<InstallRecipe> generateAdvancedRecipes(String[] names) {
		return Arrays.asList(new InstallRecipe[]{
			new InstallRecipe(names[0]).setAdvancedName("runlinux").setRunScript("run/simple_linux.sh"),
			new InstallRecipe(names[1]).setAdvancedName("reinstall"),
			new InstallRecipe(names[2])
		});
	}
}
