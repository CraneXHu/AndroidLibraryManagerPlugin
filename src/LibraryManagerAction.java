import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by pkhope on 2016/5/19.
 */
public class LibraryManagerAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Project project = e.getProject();

        LibraryManagerDialog dialog = new LibraryManagerDialog(project);
        dialog.show();
        if (dialog.isOK()){
            dialog.save();
        }
    }
}
