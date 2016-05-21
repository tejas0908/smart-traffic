package com.mindtree.techngage.entity;

import java.util.List;

/**
 * This class defines the machine learning response object.
 * Created by tejas0908 on 20/05/16.
 */
public class MLResponse {
    private List<MLItem> result;

    public List<MLItem> getResult() {
        return result;
    }

    public void setResult(List<MLItem> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "MLResponse{" +
                "result=" + result +
                '}';
    }
}
