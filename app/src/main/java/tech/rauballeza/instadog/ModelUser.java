package tech.rauballeza.instadog;

public class ModelUser {
    //Use same name as in firebase database
    String date_of_birth, email, gender, image, name, phone, search, uid, username;

    public ModelUser() {

    }

    public ModelUser(String date_of_birth, String email, String gender, String image, String name, String phone, String search, String uid, String username) {
        this.date_of_birth = date_of_birth;
        this.email = email;
        this.gender = gender;
        this.image = image;
        this.name = name;
        this.phone = phone;
        this.search = search;
        this.uid = uid;
        this.username = username;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
