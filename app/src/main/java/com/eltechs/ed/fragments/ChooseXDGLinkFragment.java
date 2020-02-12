package com.eltechs.ed.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnDismissListener;
import android.widget.TextView;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.ed.R;
import com.eltechs.ed.XDGLink;
import com.eltechs.ed.guestContainers.GuestContainer;
import com.eltechs.ed.guestContainers.GuestContainersManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChooseXDGLinkFragment extends Fragment {
    public static final String ARG_IS_START_MENU = "IS_START_MENU";
    private static final String PARENT_DIR_NAME = "..";
    private static final int VIEW_TYPE_FOLDER = 1;
    private static final int VIEW_TYPE_LINK = 0;
    private List<GuestContainer> mContainers;
    /* access modifiers changed from: private */
    public List<XDGNode> mCurrentItems;
    /* access modifiers changed from: private */
    public XDGNode mCurrentNode;
    /* access modifiers changed from: private */
    public int mDepth;
    private TextView mEmptyTextView;
    /* access modifiers changed from: private */
    public GuestContainersManager mGcm;
    /* access modifiers changed from: private */
    public boolean mIsStartMenu;
    /* access modifiers changed from: private */
    public OnXDGLinkSelectedListener mListener;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;

    public interface OnXDGLinkSelectedListener {
        void onXDGLinkSelected(XDGLink xDGLink);
    }

    private class XDGNode implements Comparable<XDGNode> {
        GuestContainer mCont;
        File mFile;
        XDGLink mLink;

        XDGNode(GuestContainer guestContainer, File file, XDGLink xDGLink) {
            this.mCont = guestContainer;
            this.mFile = file;
            this.mLink = xDGLink;
        }

        public boolean isUpNode() {
            return this.mFile.getPath().equals(ChooseXDGLinkFragment.PARENT_DIR_NAME);
        }

        public int compareTo(@NonNull XDGNode xDGNode) {
            if (isUpNode()) {
                return -1;
            }
            if (this.mFile.isDirectory() && !xDGNode.mFile.isDirectory()) {
                return -1;
            }
            if (this.mFile.isDirectory() || !xDGNode.mFile.isDirectory()) {
                return this.mFile.compareTo(xDGNode.mFile);
            }
            return 1;
        }

        public String toString() {
            if (isUpNode()) {
                return ChooseXDGLinkFragment.PARENT_DIR_NAME;
            }
            if (this.mFile.isDirectory()) {
                return this.mFile.getName();
            }
            Assert.state(this.mLink.name != null);
            return this.mLink.name;
        }
    }

    private class XDGNodeAdapter extends Adapter<XDGNodeAdapter.ViewHolder> {
        /* access modifiers changed from: private */
        public final List<XDGNode> mItems;

        public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
            public ImageButton mButton;
            public ImageView mImage;
            public XDGNode mItem;
            public TextView mSubText;
            public TextView mText;
            public final View mView;

            public ViewHolder(View view, int i) {
                super(view);
                this.mView = view;
                this.mImage = (ImageView) view.findViewById(R.id.image);
                this.mText = (TextView) view.findViewById(R.id.text);
                this.mSubText = (TextView) view.findViewById(R.id.subtext);
                this.mButton = (ImageButton) view.findViewById(R.id.button);
                if (i == 1) {
                    this.mButton.setVisibility(8);
                }
            }
        }

        public XDGNodeAdapter(List<XDGNode> list) {
            this.mItems = list;
        }

        public final ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.basic_list_item_with_button, viewGroup, false), i);
        }

        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            viewHolder.mItem = (XDGNode) this.mItems.get(i);
            if (viewHolder.mItem.mLink == null) {
                viewHolder.mImage.setImageResource(R.drawable.ic_folder_open_24dp);
            } else {
                viewHolder.mImage.setImageDrawable(new BitmapDrawable(viewHolder.mView.getResources(), ChooseXDGLinkFragment.this.mGcm.getIconPath(viewHolder.mItem.mLink)));
                viewHolder.mImage.setScaleX(0.9f);
                viewHolder.mImage.setScaleY(0.9f);
            }
            viewHolder.mText.setText(viewHolder.mItem.toString());
            viewHolder.mSubText.setText(viewHolder.mItem.isUpNode() ? "" : viewHolder.mItem.mCont.mConfig.getName());
            viewHolder.mView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    XDGNode xDGNode = (XDGNode) XDGNodeAdapter.this.mItems.get(viewHolder.getAdapterPosition());
                    if (xDGNode.mLink == null) {
                        if (xDGNode.isUpNode()) {
                            ChooseXDGLinkFragment.this.mDepth = ChooseXDGLinkFragment.this.mDepth - 1;
                            ChooseXDGLinkFragment.this.mCurrentNode = new XDGNode(ChooseXDGLinkFragment.this.mCurrentNode.mCont, new File(ChooseXDGLinkFragment.this.mCurrentNode.mFile.getParent()), null);
                        } else {
                            ChooseXDGLinkFragment.this.mDepth = ChooseXDGLinkFragment.this.mDepth + 1;
                            ChooseXDGLinkFragment.this.mCurrentNode = new XDGNode(xDGNode.mCont, xDGNode.mFile, null);
                        }
                        ChooseXDGLinkFragment.this.mCurrentItems = ChooseXDGLinkFragment.this.getCurrentNodeContent();
                        ChooseXDGLinkFragment.this.mRecyclerView.setAdapter(new XDGNodeAdapter(ChooseXDGLinkFragment.this.mCurrentItems));
                        return;
                    }
                    ChooseXDGLinkFragment.this.mListener.onXDGLinkSelected(xDGNode.mLink);
                }
            });
            viewHolder.mButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    final XDGNode xDGNode = (XDGNode) XDGNodeAdapter.this.mItems.get(viewHolder.getAdapterPosition());
                    PopupMenu popupMenu = new PopupMenu(ChooseXDGLinkFragment.this.getContext(), view);
                    popupMenu.inflate(ChooseXDGLinkFragment.this.mIsStartMenu ? R.menu.startmenu_xdg_popup_menu : R.menu.desktop_xdg_popup_menu);
                    final GuestContainer guestContainer = xDGNode.mCont;
                    if (!(guestContainer == null || guestContainer.mConfig.getRunGuide() == null || guestContainer.mConfig.getRunGuide().isEmpty())) {
                        popupMenu.getMenu().add("Show run guide").setOnMenuItemClickListener(new OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                ContainerRunGuideDFragment.createDialog(guestContainer, true).show(ChooseXDGLinkFragment.this.getActivity().getSupportFragmentManager(), "CONT_RUN_GUIDE");
                                return true;
                            }
                        });
                    }
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.xdg_copy_to_desktop /*2131296542*/:
                                    ChooseXDGLinkFragment.this.mGcm.copyXDGLinkToDesktop(xDGNode.mLink);
                                    break;
                                case R.id.xdg_delete_shortcut /*2131296543*/:
                                    Builder builder = new Builder(ChooseXDGLinkFragment.this.getActivity());
                                    builder.setTitle((CharSequence) "Shortcut deletion");
                                    builder.setIcon((int) R.drawable.ic_warning_24dp);
                                    builder.setMessage((CharSequence) "This will only delete shortcut, not application or associated container.\n\nDelete shortcut?");
                                    builder.setPositiveButton((CharSequence) "Delete", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            xDGNode.mLink.linkFile.delete();
                                            ChooseXDGLinkFragment.this.refresh();
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    builder.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    builder.show();
                                    break;
                            }
                            return true;
                        }
                    });
                    popupMenu.setOnDismissListener(new OnDismissListener() {
                        public void onDismiss(PopupMenu popupMenu) {
                            ChooseXDGLinkFragment.this.refresh();
                        }
                    });
                    popupMenu.show();
                }
            });
        }

        public int getItemViewType(int i) {
            return ((XDGNode) this.mItems.get(i)).mLink == null ? 1 : 0;
        }

        public final int getItemCount() {
            return this.mItems.size();
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (OnXDGLinkSelectedListener) context;
        } catch (ClassCastException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append(context.toString());
            sb.append(" must implement OnStartMenuLinkSelectedListener");
            throw new ClassCastException(sb.toString());
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        FrameLayout frameLayout = (FrameLayout) layoutInflater.inflate(R.layout.basic_list, viewGroup, false);
        this.mRecyclerView = (RecyclerView) frameLayout.findViewById(R.id.list);
        this.mEmptyTextView = (TextView) frameLayout.findViewById(R.id.empty_text);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mRecyclerView.getContext()));
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(this.mRecyclerView.getContext(), 1));
        return frameLayout;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mGcm = GuestContainersManager.getInstance(getContext());
        this.mContainers = this.mGcm.getContainersList();
        this.mIsStartMenu = getArguments().getBoolean(ARG_IS_START_MENU);
        this.mDepth = 0;
        this.mCurrentNode = null;
        this.mCurrentItems = getCurrentNodeContent();
        if (this.mCurrentItems.isEmpty()) {
            this.mEmptyTextView.setVisibility(0);
        } else {
            this.mRecyclerView.setAdapter(new XDGNodeAdapter(this.mCurrentItems));
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(this.mIsStartMenu ? R.string.wd_title_start_menu : R.string.wd_title_desktop);
    }

    /* access modifiers changed from: 0000 */
    public List<XDGNode> getRootNodeContent() {
        GuestContainer currentContainer = this.mGcm.getCurrentContainer();
        if (currentContainer != null) {
            return getNodeContent(new XDGNode(currentContainer, new File(this.mIsStartMenu ? currentContainer.mStartMenuPath : currentContainer.mDesktopPath), null), true);
        }
        ArrayList arrayList = new ArrayList();
        for (GuestContainer guestContainer : this.mContainers) {
            arrayList.addAll(getNodeContent(new XDGNode(guestContainer, new File(this.mIsStartMenu ? guestContainer.mStartMenuPath : guestContainer.mDesktopPath), null), true));
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    public List<XDGNode> getNodeContent(XDGNode xDGNode, boolean z) {
        ArrayList arrayList = new ArrayList();
        if (!z) {
            arrayList.add(new XDGNode(xDGNode.mCont, new File(PARENT_DIR_NAME), null));
        }
        File[] listFiles = xDGNode.mFile.listFiles();
        if (listFiles == null) {
            return arrayList;
        }
        for (File file : listFiles) {
            if (file.isDirectory()) {
                arrayList.add(new XDGNode(xDGNode.mCont, file, null));
            } else if (file.getName().toLowerCase().endsWith(".desktop")) {
                try {
                    arrayList.add(new XDGNode(xDGNode.mCont, file, new XDGLink(xDGNode.mCont, file)));
                } catch (IOException unused) {
                }
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    public List<XDGNode> getCurrentNodeContent() {
        List<XDGNode> list;
        if (this.mDepth == 0) {
            list = getRootNodeContent();
        } else {
            list = getNodeContent(this.mCurrentNode, false);
        }
        Collections.sort(list);
        return list;
    }

    /* access modifiers changed from: private */
    public void refresh() {
        if (this.mCurrentNode == null || !this.mCurrentNode.mFile.exists()) {
            this.mDepth = 0;
            this.mCurrentNode = null;
            this.mCurrentItems = getCurrentNodeContent();
        } else {
            this.mCurrentItems = getCurrentNodeContent();
        }
        if (this.mCurrentItems.isEmpty()) {
            this.mEmptyTextView.setVisibility(0);
        }
        this.mRecyclerView.setAdapter(new XDGNodeAdapter(this.mCurrentItems));
    }
}
