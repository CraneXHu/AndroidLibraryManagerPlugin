package model;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import entity.ListItem;
import listerner.OnListChangeListener;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pkhope on 2016/5/21.
 */
public class LibraryModelImpl implements LibraryModel {

    private Project mProject;
    private String mModule;
    private String mFilePath;
    protected List<ListItem> mList;

    public LibraryModelImpl(Project project){
        super();
        mProject = project;
        mList = new ArrayList<>();
        mFilePath = PathManager.getConfigPath() + "\\plugins" + "\\AndroidLibraryManager";
    }

    @Override
    public void load(OnListChangeListener listener){
        loadLibrary(listener);
        loadScript(listener);
    }

    @Override
    public void save(OnListChangeListener listener){
        saveLibrary(listener);
        saveScript(listener);
    }

    protected void loadLibrary(OnListChangeListener listener){
        File appDir = new File(mFilePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        File file = new File(appDir,"library.dat");
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String s;
            while((s = in.readLine()) != null){
                addItem(s);
            }
            in.close();
        }catch (IOException e){
            listener.onError("Open Library Failed.");
            e.printStackTrace();
        }
        listener.onSuccess(mList);
    }

    protected void saveLibrary(OnListChangeListener listener){
        String fileName = mFilePath + "\\library.dat";
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName,false)));
            for(ListItem item : mList){
                out.println(item.getName());
            }
            out.close();
        }catch (IOException e){
            listener.onError("Save Library Failed.");
            e.printStackTrace();
        }
        listener.onSuccess(mList);
    }

    @Override
    public void loadScript(OnListChangeListener listener) {

        Document document = getModuleScript();
        String text = document.getText();
        Pattern pattern = Pattern.compile("compile '(.+?)'");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()){
            String name = matcher.group(1);
            for (ListItem item : mList){
                if (item.getName().equals(name)){
                    item.setSelected(true);
                }
            }
        }

        listener.onSuccess(mList);
    }

    protected void saveScript(OnListChangeListener listener){

        Document document = getModuleScript();
        new WriteCommandAction.Simple(mProject){
            @Override
            protected void run() throws Throwable {
                String text = document.getText();
                int index = text.indexOf("dependencies");
                index += "dependencies".length();
                String result = text.substring(0,index);
                result += " {" + "\n";
                result += "    compile fileTree(dir: 'libs', include: ['*.jar'])" + "\n";
                for (ListItem item : mList){
                    if (item.getSelected()){
                        result += "    compile " + "\'" + item.getName() + "\'" + "\n";
                    }
                }
                result += "}";
                document.setText(result);
            }
        }.execute();
    }

    @Override
    public void setModule(String module) {
        mModule = module;
    }

    @Override
    public void setSelected(int index, boolean selected) {
        mList.get(index).setSelected(selected);
    }

    @Override
    public void addItem(String name) {
        ListItem item = new ListItem();
        item.setName(name);
        mList.add(item);
    }

    @Override
    public void removeItem(int index) {
        mList.remove(index);
    }

    @Override
    public void clearState(OnListChangeListener listener) {
        for (ListItem item : mList){
            item.setSelected(false);
        }
    }

    @Override
    public void clear() {
        mList.clear();
    }

    protected Document getModuleScript(){
        Module module = ModuleManager.getInstance(mProject).findModuleByName(mModule);
        VirtualFile root = module.getModuleFile().getParent();
        VirtualFile script = root.findChild("build.gradle");
        Document document = FileDocumentManager.getInstance().getDocument(script);
        return document;
    }
}
