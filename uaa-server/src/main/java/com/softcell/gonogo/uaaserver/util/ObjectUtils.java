package com.softcell.gonogo.uaaserver.util;

import lombok.Data;
import org.springframework.beans.BeanUtils;

public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {


    public static <T> T combineTwoObject(T existingObject , T objectPayload) throws IllegalAccessException, InstantiationException {

        T t = (T) existingObject.getClass().newInstance();

        BeanUtils.copyProperties(existingObject,t);
        BeanUtils.copyProperties(objectPayload,t);

        return t;

    }

    @Data
    static class Person {
        String id;
        String name;
        Address address;

    }

    @Data
    static class Address{
        String addressLine1;
        String addressLine2;

    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {

        Person person = new Person();
        person.setId("1");
        person.setName("prateek");
        Address address = new Address();
        address.setAddressLine1("pune");
        address.setAddressLine2("baner");
        person.setAddress(address);

        Person person1 = new Person();
        person1.setId("2");
        //person1.setName("mohit");
        Address address1 = new Address();
        //address1.setAddressLine1("delhi");
        address1.setAddressLine2("babrala");
        person1.setAddress(address1);

        Person person2 = ObjectUtils.combineTwoObject(person, person1);

        System.out.println(person2);


    }

}
