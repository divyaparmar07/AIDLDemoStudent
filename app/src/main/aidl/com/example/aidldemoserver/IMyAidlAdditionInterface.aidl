// IMyAidlAdditionInterface.aidl
package com.example.aidldemoserver;

// Declare any non-default types here with import statements

interface IMyAidlAdditionInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int calculateAddition(int a,int b);
    int calculateSubtraction(int a,int b);
    int getRandomNumber();
    int sumOfArray(in int[] intArray);
    String concateString(in String[] strArray);
    boolean basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString);
}