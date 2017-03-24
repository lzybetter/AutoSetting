package shanghai.lzybetter.autosetting.Class;

/**
 * Created by lzybetter on 2017/3/9.
 */

public class Choose_Action {

    private String action_title;
    private String if_title;
    private int type;
    private int if_type;

    public Choose_Action(int type,String action_title,String if_title,int if_type) {
        this.action_title = action_title;
        this.if_title = if_title;
        this.type = type;
        this.if_type = if_type;
    }

    public String getAction_title() {
        return action_title;
    }

    public int getType() {
        return type;
    }

    public int getIf_type() {
        return if_type;
    }

    public String getIf_title() {
        return if_title;
    }
}
