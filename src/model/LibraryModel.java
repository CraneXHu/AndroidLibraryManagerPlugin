package model;

import listerner.OnListChangeListener;

/**
 * Created by pkhope on 2016/5/21.
 */
public interface LibraryModel {

    void load(OnListChangeListener listener);

    void loadScript(OnListChangeListener listener);

    void save(OnListChangeListener listener);

    void addItem(String name);

    void removeItem(int index);

    void setSelected(int index, boolean selected);

    void clearState(OnListChangeListener listener);

    void clear();

    void setModule(String module);
}
