package com.eltechs.axs.widgets.actions;

import com.eltechs.axs.helpers.Assert;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActionGroup {
    private boolean exclusive;
    private final List<Action> members = new ArrayList();

    public void add(Action action) {
        this.members.add(action);
        checkCheckableStatusConsistency();
    }

    public void remove(Action action) {
        this.members.remove(action);
    }

    public boolean isCheckable() {
        if (this.members.isEmpty()) {
            return false;
        }
        return ((Action) this.members.get(0)).isCheckable();
    }

    public boolean isExclusive() {
        return this.exclusive;
    }

    public void setExclusive(boolean z) {
        this.exclusive = z;
    }

    public List<Action> getMembers() {
        return Collections.unmodifiableList(this.members);
    }

    private void checkCheckableStatusConsistency() {
        boolean isCheckable = ((Action) this.members.get(0)).isCheckable();
        for (Action isCheckable2 : this.members) {
            Assert.state(isCheckable2.isCheckable() == isCheckable, "All members of an ActionGroup must be simultaneously checkable or non-checkable.");
        }
    }
}
