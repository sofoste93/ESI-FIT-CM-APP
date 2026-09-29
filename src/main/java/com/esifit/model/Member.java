package com.esifit.model;

import java.time.LocalDate;

public record Member(
        String id,
        String firstName,
        String lastName,
        String email,
        String plan,
        LocalDate joinedOn,
        boolean active
) {
    public String fullName() {
        return firstName + " " + lastName;
    }

    public String initials() {
        return (firstName.substring(0, 1) + lastName.substring(0, 1)).toUpperCase();
    }
}
