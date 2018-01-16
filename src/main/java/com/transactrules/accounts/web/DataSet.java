package com.transactrules.accounts.web;

import java.util.ArrayList;
import java.util.List;

public class DataSet {
    public Integer dataSetId;
    public String dataSetName;
    public String  url;
    public boolean isValid;
    public  Integer[] dependsOnDataSets = {};
    public List<DataElement> data = new ArrayList<>();
}
