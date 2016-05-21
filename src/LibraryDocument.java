import com.intellij.openapi.application.PathManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pkhope on 2016/5/21.
 */
public class LibraryDocument {

    private List<String> mLibraryList;
    private String mFilePath;

    LibraryDocument(){
        mLibraryList = new ArrayList<>();
        mFilePath = PathManager.getConfigPath() + "\\plugins" + "\\AndroidLibraryManager";
    }

    public void open(){
//        Messages.showMessageDialog(mProject,pluginPath,"Path",null);
        File appDir = new File(mFilePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        File file = new File(appDir,"library.dat");
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String s;
            while((s = in.readLine()) != null){
                mLibraryList.add(s);
            }
            in.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void save(){
        String fileName = mFilePath + "\\library.dat";
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName,false)));
            for(String libraryName : mLibraryList){
                out.println(libraryName);
            }
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public List<String> getLibraryList(){
        return mLibraryList;
    }

    public void setLibraryList(List<String> list){
        mLibraryList = list;
    }
}
