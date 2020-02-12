package com.eltechs.ed.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;
import com.eltechs.axs.helpers.AndroidHelpers;
import com.eltechs.ed.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChooseFileFragment extends Fragment {
    public static final String ARG_DOWNLOAD_URL = "DOWNLOAD_URL";
    public static final String ARG_ROOT_PATH = "ROOT_PATH";
    /* access modifiers changed from: private */
    public static final String NO_EXE_FILES = AndroidHelpers.getString(R.string.wd_no_exe_files);
    private static final String PARENT_DIR_NAME = "..";
    private static final int VIEW_TYPE_NO_EXE = 1;
    private static final int VIEW_TYPE_REGULAR = 0;
    /* access modifiers changed from: private */
    public File mCurrentDir;
    /* access modifiers changed from: private */
    public List<File> mCurrentItems;
    private String mDownloadUrl;
    /* access modifiers changed from: private */
    public OnFileSelectedListener mListener;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    /* access modifiers changed from: private */
    public File mRootDir;

    private class FileAdapter extends Adapter<FileAdapter.ViewHolder> {
        /* access modifiers changed from: private */
        public final List<File> mItems;

        public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
            public ImageView mImage;
            public File mItem;
            public TextView mText;
            public final View mView;

            public ViewHolder(View view, int i) {
                super(view);
                this.mView = view;
                this.mImage = (ImageView) view.findViewById(R.id.image);
                this.mText = (TextView) view.findViewById(R.id.text);
                if (i == 1) {
                    this.mImage.setVisibility(8);
                    this.mText.setText(ChooseFileFragment.NO_EXE_FILES);
                    this.mText.setGravity(17);
                    this.mText.setPadding(0, 0, 0, 0);
                    this.mView.setClickable(false);
                }
            }
        }

        public FileAdapter(List<File> list) {
            this.mItems = list;
        }

        public final ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.basic_list_item, viewGroup, false), i);
        }

        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            if (getItemViewType(i) != 1) {
                viewHolder.mItem = (File) this.mItems.get(i);
                viewHolder.mImage.setImageResource((viewHolder.mItem.getPath().equals(ChooseFileFragment.PARENT_DIR_NAME) || viewHolder.mItem.isDirectory()) ? R.drawable.ic_folder_open_24dp : R.drawable.ic_description_24dp);
                viewHolder.mText.setText(viewHolder.mItem.getName());
                viewHolder.mView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        File file = (File) FileAdapter.this.mItems.get(viewHolder.getAdapterPosition());
                        if (file.getPath().equals(ChooseFileFragment.PARENT_DIR_NAME)) {
                            ChooseFileFragment.this.mCurrentDir = ChooseFileFragment.this.mCurrentDir.getParentFile();
                            ChooseFileFragment.this.mCurrentItems = ChooseFileFragment.this.getDirContent(ChooseFileFragment.this.mCurrentDir, ChooseFileFragment.this.mRootDir);
                            ChooseFileFragment.this.mRecyclerView.setAdapter(new FileAdapter(ChooseFileFragment.this.mCurrentItems));
                        } else if (file.isDirectory()) {
                            ChooseFileFragment.this.mCurrentDir = file;
                            ChooseFileFragment.this.mCurrentItems = ChooseFileFragment.this.getDirContent(ChooseFileFragment.this.mCurrentDir, ChooseFileFragment.this.mRootDir);
                            ChooseFileFragment.this.mRecyclerView.setAdapter(new FileAdapter(ChooseFileFragment.this.mCurrentItems));
                        } else {
                            ChooseFileFragment.this.mListener.onFileSelected(file.getAbsolutePath());
                        }
                    }
                });
            }
        }

        public int getItemViewType(int i) {
            return ((File) this.mItems.get(i)).getPath().equals(ChooseFileFragment.NO_EXE_FILES) ? 1 : 0;
        }

        public final int getItemCount() {
            return this.mItems.size();
        }
    }

    public interface OnFileSelectedListener {
        void onFileSelected(String str);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (OnFileSelectedListener) context;
        } catch (ClassCastException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append(context.toString());
            sb.append(" must implement OnFileSelectedListener");
            throw new ClassCastException(sb.toString());
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mDownloadUrl = getArguments().getString(ARG_DOWNLOAD_URL);
        if (this.mDownloadUrl != null) {
            setHasOptionsMenu(true);
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.choose_install_file_menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.choose_install_file_download) {
            return super.onOptionsItemSelected(menuItem);
        }
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.mDownloadUrl)));
        return true;
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
        this.mRootDir = new File(getArguments().getString(ARG_ROOT_PATH));
        this.mCurrentDir = this.mRootDir;
        this.mCurrentItems = getDirContent(this.mCurrentDir, this.mRootDir);
        this.mRecyclerView.setAdapter(new FileAdapter(this.mCurrentItems));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((int) R.string.wd_title_select_installer_file);
    }

    /* access modifiers changed from: 0000 */
    public List<File> getDirContent(File file, File file2) {
        File[] listFiles;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (!file.equals(file2)) {
            arrayList.add(new File(PARENT_DIR_NAME));
        }
        for (File file3 : file.listFiles()) {
            if (file3.isDirectory()) {
                arrayList.add(file3);
            } else if (file3.getName().toLowerCase().endsWith(".exe") || file3.getName().toLowerCase().endsWith(".msi") || file3.getName().toLowerCase().endsWith(".bat")) {
                arrayList2.add(file3);
            }
        }
        Collections.sort(arrayList);
        Collections.sort(arrayList2);
        arrayList.addAll(arrayList2);
        if (arrayList2.isEmpty()) {
            arrayList.add(new File(NO_EXE_FILES));
        }
        return arrayList;
    }
}
