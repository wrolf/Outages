package net.wrolf.android.outages;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Activity for holding OutageListFragment.
 */
public class OutageListActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outage_list);
    }
}
