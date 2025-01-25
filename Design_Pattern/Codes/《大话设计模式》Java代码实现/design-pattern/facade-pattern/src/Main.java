import facade.Facade;

public class Main {
    public static void main(String[] args) {
        Facade facade = new Facade();
        // 14级员工薪资待遇
        System.out.println("level 14 without tax: " + facade.getSalaryWithoutTax(14));
        System.out.println("level 14 with tax: " + facade.getSalaryWithTax(14));
        // 15级员工薪资待遇
        System.out.println("level 15 without tax: " + facade.getSalaryWithoutTax(15));
        System.out.println("level 15 with tax: " + facade.getSalaryWithTax(15));
    }
}