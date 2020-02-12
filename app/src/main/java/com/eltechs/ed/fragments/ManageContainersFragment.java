package com.eltechs.ed.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnDismissListener;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.ed.R;
import com.eltechs.ed.guestContainers.GuestContainer;
import com.eltechs.ed.guestContainers.GuestContainersManager;
import java.util.List;
import com.eltechs.axs.activities.*;

public class ManageContainersFragment extends Fragment {
    private static final int CONT_ASYNC_ACTION_CLONE = 1;
    private static final int CONT_ASYNC_ACTION_DELETE = 2;
    private static final int CONT_ASYNC_ACTION_NEW = 0;
    private List<GuestContainer> mContainers;
    private TextView mEmptyTextView;
    /* access modifiers changed from: private */
    public GuestContainersManager mGcm;
    /* access modifiers changed from: private */
    public boolean mIsAsyncTaskRun;
    /* access modifiers changed from: private */
    public OnManageContainersActionListener mListener;
    /* access modifiers changed from: private */
    public ProgressDialog mProgressDialog;
    /* access modifiers changed from: private */
    public String mProgressMessage;
    private RecyclerView mRecyclerView;

    private class ContAsyncTask extends AsyncTask<GuestContainer, Void, Void> {
        private int mAction;

        public ContAsyncTask(int i) {
            this.mAction = i;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            ManageContainersFragment.this.mIsAsyncTaskRun = true;
            switch (this.mAction) {
                case 0:
                    ManageContainersFragment.this.mProgressMessage = "Creating container...";
                    break;
                case 1:
                    ManageContainersFragment.this.mProgressMessage = "Cloning container...";
                    break;
                case 2:
                    ManageContainersFragment.this.mProgressMessage = "Deleting container...";
                    break;
                default:
                    Assert.state(false);
                    break;
            }
            ManageContainersFragment.this.mProgressDialog = ManageContainersFragment.this.showProgressDialog(ManageContainersFragment.this.mProgressMessage);
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(GuestContainer... guestContainerArr) {
            switch (this.mAction) {
                case 0:
                    ManageContainersFragment.this.mGcm.createContainer();
                    break;
                case 1:
                    ManageContainersFragment.this.mGcm.cloneContainer(guestContainerArr[0]);
                    break;
                case 2:
                    ManageContainersFragment.this.mGcm.deleteContainer(guestContainerArr[0]);
                    break;
                default:
                    Assert.state(false);
                    break;
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void voidR) {
            ManageContainersFragment.this.refreshContainersList();
            if (ManageContainersFragment.this.mProgressDialog != null) {
                ManageContainersFragment.this.closeProgressDialog(ManageContainersFragment.this.mProgressDialog);
            }
            ManageContainersFragment.this.mIsAsyncTaskRun = false;
        }
    }

    private class ContainersAdapter extends Adapter<ContainersAdapter.ViewHolder> {
        /* access modifiers changed from: private */
        public final List<GuestContainer> mItems;

        public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
            public ImageButton mButton;
            public ImageView mImage;
            public GuestContainer mItem;
            public TextView mSubText;
            public TextView mText;
            public final View mView;

            public ViewHolder(View view) {
                super(view);
                this.mView = view;
                this.mImage = (ImageView) view.findViewById(R.id.image);
                this.mText = (TextView) view.findViewById(R.id.text);
                this.mSubText = (TextView) view.findViewById(R.id.subtext);
                this.mButton = (ImageButton) view.findViewById(R.id.button);
                this.mText.setGravity(16);
                this.mSubText.setVisibility(8);
                this.mView.setClickable(false);
            }
        }

        public ContainersAdapter(List<GuestContainer> list) {
            this.mItems = list;
        }

        public final ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.basic_list_item_with_button, viewGroup, false));
        }

        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            viewHolder.mItem = (GuestContainer) this.mItems.get(i);
            viewHolder.mImage.setImageResource(R.drawable.ic_archive_24dp);
            viewHolder.mText.setText(viewHolder.mItem.mConfig.getName());
            if (ManageContainersFragment.this.mGcm.getCurrentContainer() != null && viewHolder.mItem == ManageContainersFragment.this.mGcm.getCurrentContainer()) {
                viewHolder.mView.setBackgroundResource(R.color.primary_light);
            }
            viewHolder.mButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    final GuestContainer guestContainer = (GuestContainer) ContainersAdapter.this.mItems.get(viewHolder.getAdapterPosition());
                    PopupMenu popupMenu = new PopupMenu(ManageContainersFragment.this.getContext(), view);
                    popupMenu.inflate(R.menu.container_popup_menu);
                    popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.container_clone /*2131296332*/:
                                    new ContAsyncTask(1).execute(new GuestContainer[]{guestContainer});
                                    break;
                                case R.id.container_delete /*2131296333*/:
                                    new ContAsyncTask(2).execute(new GuestContainer[]{guestContainer});
                                    break;
                                case R.id.container_install_package /*2131296334*/:
                                    ManageContainersFragment.this.mListener.onManageContainersInstallPackages(guestContainer);
                                    break;
                                case R.id.container_properties /*2131296335*/:
                                    ManageContainersFragment.this.mListener.onManageContainerSettingsClick(guestContainer);
                                    break;
                                case R.id.container_run_explorer /*2131296336*/:
                                    ManageContainersFragment.this.mListener.onManageContainersRunExplorer(guestContainer);
                                    break;
								case R.id.container_run_linuxenv:
									ManageContainersFragment.this.mListener.onManageContainersRunLinuxEnv(guestContainer);
									break;
                            }
                            return true;
                        }
                    });
                    popupMenu.setOnDismissListener(new OnDismissListener() {
                        public void onDismiss(PopupMenu popupMenu) {
                            ManageContainersFragment.this.refreshContainersList();
                        }
                    });
                    popupMenu.show();
                }
            });
        }

        public final int getItemCount() {
            return this.mItems.size();
        }
    }

    public interface OnManageContainersActionListener {
        void onManageContainerSettingsClick(GuestContainer guestContainer);

        void onManageContainersInstallPackages(GuestContainer guestContainer);

        void onManageContainersRunExplorer(GuestContainer guestContainer);
		
		void onManageContainersRunLinuxEnv(GuestContainer guestContainer);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (OnManageContainersActionListener) context;
        } catch (ClassCastException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append(context.toString());
            sb.append(" must implement OnManageContainersActionListener");
            throw new ClassCastException(sb.toString());
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.manage_containers_menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.manage_containers_new) {
            return super.onOptionsItemSelected(menuItem);
        }
		
        ContAsyncTask task = new ContAsyncTask(0);
        task.execute(new GuestContainer[]{null});
		
        return true;
    }
	
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        FrameLayout frameLayout = (FrameLayout) layoutInflater.inflate(R.layout.basic_list, viewGroup, false);
        this.mRecyclerView = (RecyclerView) frameLayout.findViewById(R.id.list);
        this.mEmptyTextView = (TextView) frameLayout.findViewById(R.id.empty_text);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mRecyclerView.getContext()));
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(this.mRecyclerView.getContext(), 1));
        return frameLayout;
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
        }
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mGcm = GuestContainersManager.getInstance(getContext());
        refreshContainersList();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((int) R.string.wd_title_manage_containers);
        if (this.mIsAsyncTaskRun) {
            this.mProgressDialog = showProgressDialog(this.mProgressMessage);
        }
    }

    /* access modifiers changed from: private */
    public void refreshContainersList() {
        this.mContainers = this.mGcm.getContainersList();
        this.mRecyclerView.setAdapter(new ContainersAdapter(this.mContainers));
        if (this.mContainers.isEmpty()) {
            this.mEmptyTextView.setVisibility(0);
        }
    }

    /* access modifiers changed from: private */
    public ProgressDialog showProgressDialog(String str) {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(str);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }

    /* access modifiers changed from: private */
    public void closeProgressDialog(ProgressDialog progressDialog) {
        progressDialog.dismiss();
    }
}
