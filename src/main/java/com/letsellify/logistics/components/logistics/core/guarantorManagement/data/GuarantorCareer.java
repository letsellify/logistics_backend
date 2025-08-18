package com.letsellify.logistics.components.logistics.core.guarantorManagement.data;


/**
 * Author: Ahmad Buba
 * Date:8/18/25
 */


public enum GuarantorCareer {
    CIVIL_SERVANT,
    DOCTOR,
    LAWYER,
    ENGINEER,
    ACCOUNTANT,
    BUSINESS_OWNER,
    MANAGER,
    BANKER,
    CONSULTANT,
    TEACHER,
    PROFESSOR,
    LECTURER,
    DIRECTOR;

    public static boolean isValid(String value) {
        try {
            GuarantorCareer.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
