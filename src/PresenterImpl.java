import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import java.util.List;

/**
 * Created by pkhope on 2016/5/21.
 */
public class PresenterImpl implements IPresenter, OnListChangeListener {

    private Project mProject;
    private IView mView;
    private IModel mModel;

    PresenterImpl(IView view, Project project){
        mView = view;
        mProject = project;
        mModel = new LibraryDocument(project);
    }

    @Override
    public void load() {
        mModel.load(this);
    }

    @Override
    public void save() {
        mModel.save(this);
    }

    @Override
    public void changeModule(String name) {

        mModel.clearState(this);
        mModel.setModule(name);
        mModel.loadScript(this);

    }

    @Override
    public void addItem(String name) {
        mModel.addItem(name);
    }

    @Override
    public void removeItem(int index) {
        mModel.removeItem(index);
    }

    @Override
    public void setSelected(int index, boolean selected) {
        mModel.setSelected(index,selected);
    }

    @Override
    public void onSuccess(List<ListItem> list) {
        mView.update(list);
    }

    @Override
    public void onError(String msg) {
        Messages.showErrorDialog(mProject,msg,"Error");
    }
}
