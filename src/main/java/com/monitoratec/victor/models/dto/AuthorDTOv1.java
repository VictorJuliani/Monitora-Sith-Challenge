package com.monitoratec.victor.models.dto;

import com.monitoratec.victor.models.IVersioned;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AuthorDTOv1 implements IVersioned {
    private final Integer id;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthdate;
    private final boolean distinguished;

    @Override
    public IVersioned toVersion(int version) {
        return this;
    }
}
