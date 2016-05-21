import com.intellij.ide.IdeRepaintManager;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.application.PluginPathManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.EnvironmentUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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

    protected LibraryManagerDialog(@Nullable Project project) {
        super(project);

        mProject = project;

        setTitle("Library Manager");
        initModuleCombo();
        initLibraryList();

        mAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String libraryName = Messages.showInputDialog(mProject, "Input your library name", "Add", null);
                if (libraryName.equals("")){
                    return;
                }
                mListModel.addElement(new JCheckBox(libraryName));
            }
        });

        mDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mListModel.remove(mList.getSelectedIndex());
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


        String pluginPath = PathManager.getConfigPath() + "\\plugins" + "\\AndroidLibraryManager";
//        Messages.showMessageDialog(mProject,pluginPath,"Path",null);
        File appDir = new File(pluginPath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        File file = new File(appDir,"library.dat");
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String s;
            while((s = in.readLine()) != null){
                mListModel.addElement(new JCheckBox(s));
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        mListModel.addElement(new JCheckBox("Hello world"));

    }

}
