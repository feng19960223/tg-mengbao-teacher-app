package com.turingoal.mengbao.teacher.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

import java.io.Serializable;

/**
 * 通讯录Bean
 */
public class AddressBookSectionBean extends SectionEntity<User> implements Serializable {
    private static final long serialVersionUID = 1L;

    public AddressBookSectionBean(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public AddressBookSectionBean(User t) {
        super(t);
    }
}
