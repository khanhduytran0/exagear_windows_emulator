package com.eltechs.ed.fragments.help;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.eltechs.ed.R;
import com.eltechs.ed.activities.EDHelpActivity;
import com.eltechs.ed.controls.Controls;
import com.eltechs.ed.fragments.ControlsInfoDFragment;
import java.util.List;

public class HelpControlsFragment extends Fragment {
    private RecyclerView mRecyclerView;

    private class RecipeAdapter extends Adapter<RecipeAdapter.ViewHolder> {
        private final List<Controls> mItems;

        public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
            public ImageView mImage;
            public Controls mItem;
            public TextView mText;
            public final View mView;

            public ViewHolder(View view) {
                super(view);
                this.mView = view;
                this.mImage = (ImageView) view.findViewById(R.id.image);
                this.mText = (TextView) view.findViewById(R.id.text);
            }
        }

        public RecipeAdapter(List<Controls> list) {
            this.mItems = list;
        }

        public final ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.basic_list_item, viewGroup, false));
        }

        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            viewHolder.mItem = (Controls) this.mItems.get(i);
            viewHolder.mImage.setImageResource(R.drawable.ic_touch_app_24dp);
            viewHolder.mText.setText(viewHolder.mItem.getName());
            viewHolder.mText.setTextAppearance(HelpControlsFragment.this.getContext(), 16973892);
            viewHolder.mView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ControlsInfoDFragment controlsInfoDFragment = new ControlsInfoDFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(ControlsInfoDFragment.ARG_CONTROLS_ID, viewHolder.mItem.getId());
                    controlsInfoDFragment.setArguments(bundle);
                    ((EDHelpActivity) HelpControlsFragment.this.getActivity()).setHelpFragment(controlsInfoDFragment);
                }
            });
        }

        public final int getItemCount() {
            return this.mItems.size();
        }
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
        this.mRecyclerView.setAdapter(new RecipeAdapter(Controls.getList()));
    }
}
