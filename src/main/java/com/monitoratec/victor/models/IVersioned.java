package com.monitoratec.victor.models;

public interface IVersioned {
    int LATEST = Integer.MAX_VALUE;
    IVersioned toVersion(int version);
}
