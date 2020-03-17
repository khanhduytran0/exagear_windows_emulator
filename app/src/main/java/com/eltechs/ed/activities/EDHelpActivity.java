package com.eltechs.ed.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.eltechs.axs.activities.FrameworkActivity;
import com.eltechs.ed.R;
import com.eltechs.ed.fragments.help.HelpRootFragment;

public class EDHelpActivity extends FrameworkActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.help);
        setSupportActionBar((Toolbar) findViewById(R.id.ed_help_toolbar));
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator((int) R.drawable.ic_close_24dp);
        if (bundle == null) {
            setHelpFragment(new HelpRootFragment());
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        finish();
        return true;
    }

    public void setHelpFragment(Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.ed_help_fragment_container, fragment);
        beginTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (!(fragment instanceof HelpRootFragment)) {
            beginTransaction.addToBackStack(null);
        }
        beginTransaction.commit();
    }
}
