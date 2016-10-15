package overlays;

import org.javagram.dao.Contact;

/**
 * Created by HerrSergio on 02.07.2016.
 */
public class ContactInfo {
    private String firstName;
    private String lastName;
    private String phone;
    private int id;

    public ContactInfo() {
        this("", "", "");
    }

    public ContactInfo(String phone, String firstName, String lastName) {
        this(phone, firstName, lastName, 0);
    }

    public ContactInfo(String phone, String firstName, String lastName, int id) {
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }

    public ContactInfo(Contact contact) {
        this(contact.getPhoneNumber(), contact.getFirstName(), contact.getLastName(), contact.getId());
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClearedPhone() {
        return getPhone().replaceAll("\\D+", "");
    }
}
