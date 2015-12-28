package com.jhipster.sample.web.rest.dto;

import java.util.List;

/**
 * Created by pm01 on 12/25/15.
 */
public class DataList {
    private List<?> lstObject;

    public DataList(List<?> lstObject) {
        this.lstObject = lstObject;
    }

    public DataList() {
    }

    public List<?> getLstObject() {
        return lstObject;
    }

    public void setLstObject(List<?> lstObject) {
        this.lstObject = lstObject;
    }
}
