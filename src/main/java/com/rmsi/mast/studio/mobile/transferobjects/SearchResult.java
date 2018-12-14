package com.rmsi.mast.studio.mobile.transferobjects;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {
    private int count = 0;
    private List result = null;
    
    public SearchResult(){
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }
}
