import java.util.List;

/**
 * Created by thinkpad on 2016/5/21.
 */
public interface OnListChangeListener {

    void onSuccess(List<ListItem> list);

    void onError(String msg);
}
