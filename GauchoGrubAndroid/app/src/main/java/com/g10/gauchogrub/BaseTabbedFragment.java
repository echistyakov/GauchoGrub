package com.g10.gauchogrub;

import android.app.Fragment;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabHost.TabContentFactory;

public abstract class BaseTabbedFragment extends Fragment {

    public final static String CARILLO = "Carillo";
    public final static String DLG = "DLG";
    public final static String ORTEGA = "Ortega";
    public final static String PORTOLA = "Portola";
    public final static String[] diningCommons = {CARILLO, DLG, ORTEGA, PORTOLA};

    public abstract void setDisplayContent(int tag);

    public abstract TabContentFactory createTabContent();

    public void setUpTabs(TabHost tabs, TabContentFactory contentCreate, int numTabs){
        String[] commons = new String[] {CARILLO, DLG, ORTEGA, PORTOLA};
        tabs.setup();
        //Create tabs and set text & content
        for(int i = 0; i < numTabs ; i ++) {
            TabSpec tab = tabs.newTabSpec(i + "");
            tab.setContent(contentCreate);
            tab.setIndicator(commons[i]);
            tabs.addTab(tab);
        }
        //Set tab listeners to change content when triggered
        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                setDisplayContent(Integer.parseInt(tabId));
            }});
    }
}
