package com.company.sample.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.chile.core.annotations.MetaProperty;
import javax.persistence.Transient;

@NamePattern("%s|name")
@Table(name = "SAMPLE_CUSTOMER")
@Entity(name = "sample$Customer")
public class Customer extends StandardEntity {
    private static final long serialVersionUID = -6269895397249464673L;

    @Transient
    @MetaProperty
    protected Integer lineNum;

    @Column(name = "NAME")
    protected String name;

    @Column(name = "ADDRESS")
    protected String address;


    public void setLineNum(Integer lineNum) {
        this.lineNum = lineNum;
    }

    public Integer getLineNum() {
        return lineNum;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }


}