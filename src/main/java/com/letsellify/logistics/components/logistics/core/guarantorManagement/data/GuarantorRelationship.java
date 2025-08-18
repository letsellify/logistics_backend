package com.letsellify.logistics.components.logistics.core.guarantorManagement.data;


/**
 * Author: Ahmad Buba
 * Date:8/18/25
 */


public enum GuarantorRelationship {
    PARENT,
    SPOUSE,
    CHILD,
    SIBLING,
    AUNT,
    UNCLE,
    GRANDPARENT,
    LANDLORD,
    COMMUNITY_LEADER,
    NIECE,
    NEPHEW;

    public static boolean isValid(String value) {
        try {
            GuarantorRelationship.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
