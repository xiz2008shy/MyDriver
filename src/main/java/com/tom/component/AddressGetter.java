package com.tom.component;

import com.tom.model.AddressProperty;

import java.io.File;


public interface AddressGetter {

    String getCurPath();

    File getFile();

    AddressProperty getAddressProperty();
}
