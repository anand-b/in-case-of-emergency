package edu.nandboolean.incaseofemergency.beans;

public class EmergencyContact {

    private static EmergencyContact contact = null;

    private String name;
    private String number;

    private EmergencyContact() { }

    public static synchronized EmergencyContact getEmergencyContact() {
        if (contact == null) {
            contact = new EmergencyContact();
        }
        return contact;
    }

    public EmergencyContact setName(String name) {
        this.name = name;
        return this;
    }

    public EmergencyContact setNumber(String number) {
        this.number = number;
        return this;
    }

    public void clear() {
        this.name = null;
        this.number = null;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " - " + number;
    }
}
