package product;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-12
 * @Description: 需要被构建的对象（product：定义对象的组成部分）
 */
public class Person {
    private String name;
    private String gender;
    private String address;

    public Person() {
    }

    @Override
    public String toString() {
        return name + " " + gender + " " + address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
