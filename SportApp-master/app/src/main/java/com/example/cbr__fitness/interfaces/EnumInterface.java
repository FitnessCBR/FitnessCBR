package com.example.cbr__fitness.interfaces;

/**
 * Used to enable methods to work on different enums that use a label and a id.
 */
public interface EnumInterface {

    public int getID();

    public String getLabel();

    public EnumInterface[] valuesList();
}
