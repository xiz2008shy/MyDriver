package com.tom.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileRecord implements Serializable {

    private long id;

    private String fileName;

    private String relativeLocation;

    private long baseId;

}
