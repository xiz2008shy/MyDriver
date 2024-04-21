package com.tom.entity;

import com.dream.system.annotation.Column;
import com.dream.system.annotation.Id;
import com.dream.system.annotation.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Table("file_record_table")
public class FileRecord2 implements Serializable {

    @Id
    @Column
    private long id;
    @Column("file_name")
    private String fileName;
    @Column("relative_location")
    private String relativeLocation;
    @Column("base_id")
    private long baseId;

    public FileRecord2() {
    }
}
