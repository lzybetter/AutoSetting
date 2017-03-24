package shanghai.lzybetter.autosetting.Class;

import org.litepal.crud.DataSupport;

/**
 * Created by lzybetter on 2017/3/9.
 */

public class If_Action_Save extends DataSupport{

    private int id;

    private int type;
    private String if_title;
    private String action_title;
    private int action_type;

    public int getAction_type() {
        return action_type;
    }

    public void setAction_type(int action_type) {
        this.action_type = action_type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIf_title() {
        return if_title;
    }

    public void setIf_title(String if_title) {
        this.if_title = if_title;
    }

    public String getAction_title() {
        return action_title;
    }

    public void setAction_title(String action_title) {
        this.action_title = action_title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
