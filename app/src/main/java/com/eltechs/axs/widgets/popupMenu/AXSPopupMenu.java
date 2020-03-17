package com.eltechs.axs.widgets.popupMenu;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.PopupMenu;
import com.eltechs.axs.helpers.AndroidFeatureTests;
import com.eltechs.axs.helpers.AndroidFeatureTests.ApiLevel;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.widgets.actions.Action;
import com.eltechs.axs.widgets.actions.ActionGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AXSPopupMenu {
    private final PopupMenu impl;
    private final List<MenuItemWrapper> menuItems = new ArrayList();

    private class ActionGroupWrapper implements MenuItemWrapper {
        /* access modifiers changed from: private */
        public final ActionGroup actionGroup;

        ActionGroupWrapper(ActionGroup actionGroup2) {
            this.actionGroup = actionGroup2;
        }

        public void addItemsToMenu(Menu menu, int i) {
            int i2 = 0;
            for (final Action action : this.actionGroup.getMembers()) {
                int i3 = i2 + 1;
                MenuItem add = menu.add(i, i2, 0, action.getName());
                add.setEnabled(action.isEnabled());
                add.setChecked(action.isChecked());
                add.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        action.run();
                        return true;
                    }
                });
                i2 = i3;
            }
            menu.setGroupCheckable(i, this.actionGroup.isCheckable(), this.actionGroup.isExclusive());
        }
    }

    private class ActionWrapper implements MenuItemWrapper {
        /* access modifiers changed from: private */
        public final Action action;

        ActionWrapper(Action action2) {
            this.action = action2;
        }

        public void addItemsToMenu(Menu menu, int i) {
            MenuItem add = menu.add(i, 0, 0, this.action.getName());
            add.setEnabled(this.action.isEnabled());
            add.setCheckable(this.action.isCheckable());
            add.setChecked(this.action.isChecked());
            add.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem menuItem) {
                    ActionWrapper.this.action.run();
                    return true;
                }
            });
        }
    }

    private interface MenuItemWrapper {
        void addItemsToMenu(Menu menu, int i);
    }

    public AXSPopupMenu(Activity activity, View view) {
        this.impl = new PopupMenu(activity, view);
    }

    public AXSPopupMenu(Activity activity, View view, int i) {
        if (AndroidFeatureTests.haveAndroidApi(ApiLevel.ANDROID_4_4)) {
            this.impl = new PopupMenu(activity, view, i);
        } else {
            this.impl = new PopupMenu(activity, view);
        }
    }

    public void add(Action action) {
        this.menuItems.add(new ActionWrapper(action));
    }

    public void add(List<? extends Action> list) {
        for (Action add : list) {
            add(add);
        }
    }

    public void add(ActionGroup actionGroup) {
        this.menuItems.add(new ActionGroupWrapper(actionGroup));
    }

    public void remove(Action action) {
        Iterator it = this.menuItems.iterator();
        while (it.hasNext()) {
            MenuItemWrapper menuItemWrapper = (MenuItemWrapper) it.next();
            if ((menuItemWrapper instanceof ActionWrapper) && ((ActionWrapper) menuItemWrapper).action == action) {
                it.remove();
                return;
            }
        }
        Assert.isTrue(false, String.format("Action %s is not a member of menu %s.", new Object[]{action, this}));
    }

    public void remove(ActionGroup actionGroup) {
        Iterator it = this.menuItems.iterator();
        while (it.hasNext()) {
            MenuItemWrapper menuItemWrapper = (MenuItemWrapper) it.next();
            if ((menuItemWrapper instanceof ActionGroupWrapper) && ((ActionGroupWrapper) menuItemWrapper).actionGroup == actionGroup) {
                it.remove();
                return;
            }
        }
        Assert.isTrue(false, String.format("Action %s is not a member of menu %s.", new Object[]{actionGroup, this}));
    }

    public void show() {
        Menu menu = this.impl.getMenu();
        menu.clear();
        int i = 0;
        for (MenuItemWrapper addItemsToMenu : this.menuItems) {
            int i2 = i + 1;
            addItemsToMenu.addItemsToMenu(menu, i);
            i = i2;
        }
        this.impl.show();
    }
}
