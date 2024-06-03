package org.example.model;

public class Address {
    private String address1;
    private String address2;
    private String town;
    private String postCode;

    public Address() {

    }

    public Address(String address1, String address2, String town, String postCode) {
        this.address1 = address1;
        this.address2 = address2;
        this.town = town;
        this.postCode = postCode;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getTown() {
        return town;
    }

    public String getPostCode() {
        return postCode;
    }
}
