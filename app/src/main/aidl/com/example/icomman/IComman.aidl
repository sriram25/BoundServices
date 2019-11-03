// IComman.aidl
package com.example.icomman;
import com.example.icomman.Person;
// Declare any non-default types here with import statements

interface IComman {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int calculate(int num1, int num2);
    List<Person> getPersonsList();

}
