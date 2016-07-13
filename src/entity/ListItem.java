package entity;

/**
 * Created by thinkpad on 2016/5/22.
 */
public class ListItem {

    private String mName;
    private boolean mSelected;

    public ListItem(){
        mName = "";
        mSelected = false;
    }

    public void setName(String name){
        mName = name;
    }

    public String getName(){
        return mName;
    }

    public void setSelected(boolean selected){
        mSelected = selected;
    }

    public boolean getSelected(){
        return mSelected;
    }
}
