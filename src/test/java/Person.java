public class Person {
    private  Integer id;
    private  String name;
    private  String surname;
    private  Integer age;
    private  String birthdate;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person that = (Person) o;
        return  id.equals(that.id) &&
                name.equals(that.name) &&
                surname.equals(that.surname) &&
                age.equals(that.age) &&
                birthdate.equals(that.birthdate);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Integer getAge() {
        return age;
    }

    public String getBirthdate() {
        return birthdate;
    }

}
