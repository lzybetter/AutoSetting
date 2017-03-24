package shanghai.lzybetter.autosetting.Class;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import shanghai.lzybetter.autosetting.Adapter.SettedAdapter;

/**
 * Created by lzybetter on 2017/3/7.
 */

public class Setted {

    private int type;
    private String list_if;
    private String action;
    private SettedAdapter settedAdapter;
    private RecyclerView recyclerView;
    private TextView textView;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public Setted(int type, String list_if, String action){
        this.type = type;
        this.list_if = list_if;
        this.action = action;
    }

    public int getType() {
        return type;
    }

    public String getList_if() {
        return list_if;
    }

    public String getAction() {
        return action;
    }

    public SettedAdapter getSettedAdapter() {
        return settedAdapter;
    }

    public void setSettedAdapter(SettedAdapter settedAdapter) {
        this.settedAdapter = settedAdapter;
    }

}
