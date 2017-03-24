package shanghai.lzybetter.autosetting.Class;

/**
 * Created by lzybetter on 2017/3/7.
 */

public class Choose_if {

    private int type;
    private String choose_list_if;

    public Choose_if(int type, String choose_list_if) {
        this.type = type;
        this.choose_list_if = choose_list_if;
    }

    public int getType() {
        return type;
    }

    public String getChoose_list_if() {
        return choose_list_if;
    }
}
