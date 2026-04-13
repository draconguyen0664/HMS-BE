package com.hms.ProfileMS.dto;

public class DoctorDropdownImpl implements DoctorDropdown {
    private final Long id;
    private final String name;

    public DoctorDropdownImpl(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}