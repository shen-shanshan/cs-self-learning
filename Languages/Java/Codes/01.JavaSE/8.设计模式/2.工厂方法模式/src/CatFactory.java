public class CatFactory implements Factory {
    public Animal createAnimal() {
        return new Cat();
    }
}
