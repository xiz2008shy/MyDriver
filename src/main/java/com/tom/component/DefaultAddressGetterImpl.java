package com.tom.component;

import com.tom.model.AddressProperty;

import java.io.File;

/**
 * @author TOMQI
 * @Description 抽象出的地址获取默认实现
 * @Date 2024/4/2 22:23
 */
public class DefaultAddressGetterImpl implements AddressGetter{

    private AddressProperty addressProperty;

    public DefaultAddressGetterImpl(AddressProperty addressProperty) {
        this.addressProperty = addressProperty;
    }

    @Override
    public String getCurPath() {
        return addressProperty.getCurPath();
    }

    @Override
    public File getFile() {
        return addressProperty.getFile();
    }

    @Override
    public AddressProperty getAddressProperty() {
        return addressProperty;
    }


}
