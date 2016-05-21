import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerImpl;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

/**
 * Created by pkhope on 2016/5/19.
 */
public class LibraryManagerDialog extends DialogWrapper {

    Project mProject;
    private JPanel mMainPanel;
    private JComboBox moduleCombo;
    private DefaultListModel mListModel;
    private MyCheckBoxList mList;
    private JPanel mPanel;
    private JButton mAdd;
    private JButton mDelete;

    private LibraryDocument mDocument;

    protected LibraryManagerDialog(@Nullable Project project) {
        super(project);

        mProject = project;
        mDocument = new LibraryDocument();
        mDocument.open();

        setTitle("Library Manager");
        initModuleCombo();
        initLibraryList();

        mAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String libraryName = Messages.showInputDialog(mProject, "Input your library name", "Add", null);
                if (libraryName == null){
                    return;
                }
                mListModel.addElement(new JCheckBox(libraryName));
//                mDocument.getLibraryList().add(libraryName);
            }
        });

        mDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = mList.getSelectedIndex();
                mListModel.remove(index);
//                mDocument.getLibraryList().remove(index);
            }
        });

        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return mMainPanel;
    }

    protected void initModuleCombo(){
        ModuleManager moduleManager = ModuleManager.getInstance(mProject);
        Module[] modules = moduleManager.getModules();
        for (Module module : modules){
            moduleCombo.addItem(module.getName());
        }

    }

    protected void initLibraryList(){

        mListModel = new DefaultListModel();
        mList = new MyCheckBoxList();
        mList.setModel(mListModel);
        mPanel.setLayout(new BoxLayout(mPanel,BoxLayout.Y_AXIS));
        mPanel.add(mList);
        mList.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        for (String libraryName : mDocument.getLibraryList()){
            mListModel.addElement(new JCheckBox(libraryName));
        }


    }

    public void save(){
        JCheckBox checkBox = null;
        List<String > checkedList = new ArrayList<>();
        int size = mListModel.size();
        for (int i = 0; i < size; i++){
            checkBox = (JCheckBox)mListModel.getElementAt(i);
            mDocument.getLibraryList().add(checkBox.getText());
            if (checkBox.isSelected()){
                checkedList.add(checkBox.getText());
            }
        }

        Module module = ModuleManager.getInstance(mProject).findModuleByName((String) moduleCombo.getSelectedItem());
        VirtualFile root = module.getModuleFile().getParent();
        VirtualFile script = root.findChild("build.gradle");
        final Document document = FileDocumentManager.getInstance().getDocument(script);
        new WriteCommandAction.Simple(mProject){
            @Override
            protected void run() throws Throwable {
                String text = document.getText();
                int index = text.indexOf("dependencies");
                index += "dependencies".length();
                String result = text.substring(0,index);
                result += " {" + "\n";
                result += "    compile fileTree(dir: 'libs', include: ['*.jar'])" + "\n";
                for (String libraryName : checkedList){
                    result += "    compile " + "\'" + libraryName + "\'" + "\n";
                }
                result += "}";
                document.setText(result);
            }
        }.execute();

        mDocument.save();
    }

}
