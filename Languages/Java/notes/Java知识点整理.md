# Java SE

## 基本语法

### 方法重载

- 方法名相同
- 参数列表不同（个数、类型、次序）

方法重载与访问修饰符、返回值类型、方法参数的名字都没有关系。

> 参考资料：
>
> - [Java中的方法重载_callmexinshou的博客-CSDN博客_java方法重载](https://blog.csdn.net/callmexinshou/article/details/123893783?ops_request_misc=&request_id=&biz_id=102&utm_term=Java中的方法重载 callme&utm_medium=distribute.pc_search_result.none-task-blog-2~all~sobaiduweb~default-0-123893783.142^v47^pc_rank_34_1,201^v3^control_2&spm=1018.2226.3001.4187)

## 面向对象

### 继承

子类可以继承父类的私有属性，但是不能继承父类私有属性的访问权限。

即子类其实是可以继承父类的所有成员的，但是父类的私有成员不能被访问，相当于是只继承了父类的非私有成员，但实际上父类的私有成员也会在子类对象中占有内存空间，只是无法使用而已。

> 参考资料：
>
> - [Java中子类能继承父类的私有属性吗？ - 百度文库 (baidu.com)](https://wenku.baidu.com/view/ce3c187b383567ec102de2bd960590c69ec3d8cf.html)

### 接口

#### 默认修饰符

- 接口的方法默认是：public + static
- 接口的成员变量默认是：public + static + final

所以一般会在接口中省略上述的修饰符。

#### 默认方法

在方法前加上 `default` 关键字，即给该方法一个默认实现，接口的实现类中可以不用重写该方法。

如果子接口继承自父接口，且都提供了一个相同名称、相同参数类型、相同返回类型的默认方法，则实现类会使用父类的版本。

如果实现类同时继承了两个接口，并且这两个接口都有一个相同名称、相同参数类型、相同返回类型的默认方法，则在子类中需要重写这个方法，否则会因为引起歧义而报错。

#### Java 8 的变化

Java 8 以后接口中可以有简单方法，即已经实现了的静态方法。

#### Java 9 的变化

Java 9 以后接口中可以有私有方法。

## 常用类与方法

### Object

#### toString()

该方法定义在 Object 这个类中，当对象被 System.out.println() 输出时，会自动调用其 toString() 方法。

### 类型转换

#### 数组 <-> List

```java
// 方式一：
List<String> list = new ArrayList<>();
String[] strs = list.toArray(new String[list.size()]);

// 方式二：
List<String> list = Arrays.asList(...);
```

#### 字符数组 -> 字符串

toString() 方法继承自其父类 Object，它只是单纯的将字符数组强制转化为字符串，因此出现了乱码。

Object 类的 toString() 方法返回一个字符串：

- 类名
- at 标记符 `@`
- 此对象哈希码的无符号十六进制表示组成

```java
// 直接使用 toString() 方法会返回乱码
char[] chars = ... ;
String s = chars.toString();
    
// 正确的转换方法
String s = String.valueOf(chars);
```

## 集合与泛型

### 集合

#### 集合框架

Collection（接口）：
- List（接口）：有序、可重复。
  - ArrayList：数组，查询快；
  - LinkedList：链表，增删快；
  - Vector：数组，线程安全，效率低，使用较少。
- Set（接口）：去重。
  - HashSet；
  - LinkedHashSet：可以记住元素插入的顺序，也可以设定成按照元素上次存取的先后来排序；
  - SortedSet（接口）：有序。
    - TreeSet。

Map（接口）：
- HashMap：线程不安全，允许 null 键、null 值；
- LinkedHashMap；
- HashTable：线程安全；
- SortedMap（接口）：有序。
  - TreeMap。

> 注意：Map 虽然并没有继承 Collection 这个接口，但是 Map 也属于集合中的一员。
>

#### 对象相等

##### （1）引用相等性

引用到堆上同一个对象（同一个内存地址）的两个引用是相等的。

此时对两个引用调用 `hashCode()` 方法会得到相同的结果。如果 `hashCode()` 方法没有被覆盖的话，默认会返回每个对象特有的序号（大部分的 Java 版本是依据内存位置计算该序号的）。

如果要判断两个引用是否相等，可以使用 `==` 来判断。

##### （2）对象相等性

堆上的两个不同对象在某种意义上是相等的，如：按名字判断两个对象是否相等。

如果你想把堆上两个不同的对象视为相等，就必须覆盖过从 Object 继承下来的 `hashCode()` 与 `equals()` 方法（确保两个对象有相同的 hashcode）。

#### HashSet 如何查重？

- 当把对象加入 HashSet 时，会使用对象的 hashcode 来判断加入的位置，并与其它的 hashcode 进行比较。若 hashcode 不同，则 HashSet 会假设这两个对象不可能相等；
- 具有相同 hashcode 的对象也不一定相等，还需要通过调用 equals() 方法进行判断，若为 false，才代表两个元素真的不相等。

示例：

```java
// 通过歌名判断是否相等
class Song {
    String title;
    String artist;
    
    public int hashCode() {
        return title.hashCode();
    }
    
    public boolean equals(Object o) {
        Song s = (Song)o;
        return getTitle().equals(s.getTitle());
    }
    // ... 
}
```

注意：

- 这里的 `hashCode()` 与 `equals()` 方法中直接使用了 String 类的 `hashCode()` 与 `equals()` 方法；
- String 类已经重写过这两个方法了：不根据地址，而是直接根据内容判断是否相等。

为什么不同对象会有相同对象的可能？

- HashSet 是使用 hashcode 来寻找符合条件的元素的，这样可以加快查找的速度，时间复杂度为 O(1)；
- hashcode 只是用来缩小寻找成本的，最后还是需要用 equals() 来确认是否真的找到了相同的元素。

#### TreeSet 如何排序？

- 若使用 TreeSet 默认的构造函数，则默认会使用对象的 compareTo() 方法来完成排序，此时要求集合中的元素必须实现 Comparable 接口；
- 也可以在构造时传入比较器。

示例：

```java
class BookCompare implements Comparator<Book> {
    // ...
}

TreeSet<Book> tree = new TreeSet<Book>(new BookCompare());
```

### 泛型

#### 基本概念

泛型，即“参数化类型”。一提到参数，最熟悉的就是定义方法时有形参，然后调用此方法时传递实参。那么参数化类型怎么理解呢？顾名思义，就是将类型由原来的具体的类型参数化，类似于方法中的变量参数，此时类型也定义成参数形式（可以称之为类型形参），然后在使用/调用时传入具体的类型（类型实参）。

泛型的本质是为了参数化类型（在不创建新的类型的情况下，通过泛型指定的不同类型来控制形参具体限制的类型）。也就是说在泛型使用过程中，操作的数据类型被指定为一个参数，这种参数类型可以用在类、接口和方法中，分别被称为泛型类、泛型接口、泛型方法。

泛型使集合具有类型安全性，它让编译器能够帮忙防止你把 Dog 加入到一群 Cat 中。

- 集合一般用 `<E>` 表示泛型，代表 element；
- 其它时候用 `<T>` 表示泛型，代表 type。

#### 特点

泛型只在编译阶段有效。

在编译之后程序会采取去泛型化的措施。也就是说 Java 中的泛型，只在编译阶段有效。在编译过程中，正确检验泛型结果后，JVM 会将泛型的相关信息擦出，并且在对象进入和离开方法的边界处添加类型检查和类型转换的方法。

也就是说，泛型信息不会进入到运行时阶段。

> 总结：泛型类型在逻辑上可以看成是多个不同的类型，但实际上都是相同的基本类型。
>

#### 使用方法

##### （1）泛型类

泛型的类型只能是类类型（包括自定义类），不能是简单类型。

定义的泛型类，就一定要传入泛型类型实参么？

并不是这样，在使用泛型的时候如果传入泛型实参，则会根据传入的泛型实参做相应的限制，此时泛型才会起到本应起到的限制作用。如果不传入泛型类型实参的话，在泛型类中使用泛型的方法或成员变量定义的类型可以为任何的类型。

不能对确切的泛型类型使用 `instanceOf` 操作，如下面的操作是非法的：

```java
if(o instanceOf Generic<Number>) {
    // ...
}
```

##### （2）泛型接口

泛型接口与泛型类的定义及使用基本相同。

在声明类的时候，需要将泛型的声明也一起加到类中。

```java
// 1.当实现泛型接口的类，未传入泛型实参：
// 即在实现时，不指定T的具体类型
// 如果不声明泛型，编译器会报错："Unknown class"，如：class FruitGenerator implements Generator<T>
// 正确形式：
class FruitGenerator<T> implements Generator<T>
    
// 示例：
class FruitGenerator<T> implements Generator<T> {
    @Override
    public T next() {
        return null;
    }
}

// 2.当实现泛型接口的类，传入泛型实参：
// 比如对于泛型接口Generator<T>，我们可以为T传入无数个实参，形成无数种类型的Generator接口
// 在实现类实现泛型接口时，如已将泛型类型传入实参类型，则所有使用泛型的地方都要替换成传入的实参类型
// 比如当将T实现为String类型时，public T next()中的T就需要替换成传入的String类型
public class FruitGenerator implements Generator<String> {
    private String[] fruits = new String[]{"Apple", "Banana", "Pear"};
    
    @Override
    public String next() {
        Random rand = new Random();
        return fruits[rand.nextInt(3)];
    }
}
```

##### （3）泛型方法

注意：

- 返回值之前的 `<T>` 非常重要，可以理解为声明此方法为泛型方法；
- 只有声明了 `<T>` 的方法才是泛型方法，泛型类中使用了泛型的成员方法并不是泛型方法；
- `<T>` 表明该方法将使用泛型类型 T，此时才可以在方法中使用泛型类型 T；
- 与泛型类的定义一样，此处 T 可以随便写为任意标识，常见的如 T、E、K、V 等形式的参数常用于表示泛型。

示例：

```java
public <T> T genericMethod(Class<T> tClass) throws InstantiationException, IllegalAccessException {
	T instance = tClass.newInstance();
	return instance;
}
```

基本用法：

```java
public class GenericTest {
   
    // 这个是一个泛型内部类
	public class Generic<T> {
        
        private T key;

        public Generic(T key) {
            this.key = key;
        }

        // 虽然在方法中使用了泛型，但是这并不是一个泛型方法，这只是类中一个普通的成员方法，只不过他的返回值是在声明泛型类时已经声明过的泛型，所以在这个方法中才可以继续使用T这个泛型
        public T getKey() {
            return key;
        }

        // 这个方法是有问题的，编译器会给我们提示这样的错误信息："cannot reslove symbol E"，因为在类的声明中并未声明泛型E，所以在使用E做形参和返回值类型时，编译器会无法识别
        public E setKey(E key) {
             this.key = keu
        }
        
    }

	// 这才是一个真正的泛型方法
    // 首先在public与返回值之间的<T>必不可少，这表明这是一个泛型方法，并且声明了一个泛型T，这个T可以出现在这个泛型方法的任意位置，泛型的数量也可以为任意多个。
    // 如：public <T,K> K showKeyName(Generic<T> container){...}
    public <T> T showKeyName(Generic<T> container) {
        T test = container.getKey();
        return test;
    }

    // 这也不是一个泛型方法，这就是一个普通的方法，只是使用了Generic<Number> 这个泛型类做形参而已。
    public void showKeyValue1(Generic<Number> obj) {
        // ...
    }

    // 这也不是一个泛型方法，这也是一个普通的方法，只不过使用了泛型通配符?，同时这也印证了泛型通配符章节所描述的?是一种类型实参，可以看做为Number等所有类的父类
    public void showKeyValue2(Generic<?> obj) {
        // ...
    }

     // 这个方法是有问题的，编译器会为我们提示错误信息："UnKnown class 'E'"，虽然我们声明了<T>，也表明了这是一个可以处理泛型的类型的泛型方法，但是这里只声明了泛型类型T，并未声明泛型类型E，因此编译器并不知道该如何处理E这个类型。
    public <T> T showKeyName(Generic<E> container){
        // ...
    }

    // 这个方法也是有问题的，编译器会为我们提示错误信息："UnKnown class 'T'"，对于编译器来说T这个类型并未在项目中声明过，因此编译也不知道该如何编译这个类
    // 如果这个方法出现在上面声明了T的泛型类中，则没有问题
    public void showkey(T genericObj){
        // ...
    }

    public static void main(String[] args) {
        // ...
    }
}
```

类中的泛型方法：

```java
public class GenericFruit {
    
    class Fruit {
        @Override
        public String toString() {
            return "fruit";
        }
    }

    class Apple extends Fruit{
        @Override
        public String toString() {
            return "apple";
        }
    }

    class Person {
        @Override
        public String toString() {
            return "Person";
        }
    }

    class GenerateTest<T> {
        
        public void show_1(T t) {
            System.out.println(t.toString());
        }

        // 在泛型类中声明了一个泛型方法，使用泛型T，注意这个T是一种全新的类型，与泛型类中声明的T不是同一种类型
        public <T> void show_2(T t) {
            // ...
        }
        
        // 在泛型类中声明了一个泛型方法，使用泛型E，泛型E可以为任意类型，可以类型与T相同，也可以不同
        // 由于泛型方法在声明的时候会声明泛型<E>，因此即使在泛型类中并未声明泛型，编译器也能够正确识别泛型方法中识别的泛型
        public <E> void show_3(E t) {
            // ...
        }
        
    }

    public static void main(String[] args) {
        
        Apple apple = new Apple();
        Person person = new Person();

        GenerateTest<Fruit> generateTest = new GenerateTest<Fruit>();
        
        // apple是Fruit的子类，所以这里可以
        generateTest.show_1(apple);
        // 下面编译器会报错，因为泛型类型实参指定的是Fruit，而传入的实参类是Person
        generateTest.show_1(person);

        // 使用这两个方法都可以成功
        generateTest.show_2(apple);
        generateTest.show_2(person);

        // 使用这两个方法也都可以成功
        generateTest.show_3(apple);
        generateTest.show_3(person);
    }
}
```

泛型方法与可变参数：

```java
public <T> void printMsg(T... args) {
    for(T t : args) {
        // ...
    }
}
```

静态方法：

- 在类中的静态方法使用泛型时，静态方法无法访问类上定义的泛型；
- 如果静态方法操作的引用数据类型不确定的时候，必须要将泛型定义在方法上。

> 总结：如果静态方法要使用泛型的话，必须将静态方法也定义成泛型方法。
>

```java
public class StaticGenerator<T> {
    // 如果在类中定义使用泛型的静态方法，需要添加额外的泛型声明（将这个方法定义成泛型方法），不可以使用泛型类中已经声明过的泛型
    // 如：public static void show(T t) {...}，此时编译器会提示错误信息："StaticGenerator cannot be refrenced from static context"
    // 正确形式：
    public static <T> void show(T t) {
        // ...
    }
}
```

泛型方法总结：

- 泛型方法使方法能独立于类而产生变化；
- 无论何时，如果你能做到，你就应该尽量使用泛型方法。也就是说，如果可以使用泛型方法将整个类泛型化，那么就应该使用泛型方法；
- 对于一个 static 的方法而言，无法访问泛型类型的参数。所以如果 static 方法要使用泛型能力，就必须使其成为泛型方法。

#### 通配符

不同版本的泛型类实例是不兼容的，可以使用通配符来代表多种不同的类型。

- 类型通配符一般是使用 `?` 代替具体的类型实参；
- 这里的 `?` 是类型实参，而不是类型形参；
- 这里的 `?` 和 Number、String、Integer 一样都是一种实际的类型，可以把 `?` 看成所有类型的父类，是一种真实的类型。

应用场景：

- 当具体类型不确定的时候，就可以用通配符 `?` 来表示泛型；
- 当操作类型时，如果不需要使用类型的具体功能，只需要使用 Object 类中的功能。那么可以用 `?` 通配符来表未知类型。

上下边界：

- 在使用泛型的时候，我们还可以为传入的泛型类型实参进行上下边界的限制，如：类型实参只准传入某种类型的父类或某种类型的子类；
- 泛型的上下边界添加，必须与泛型的声明在一起。

```java
// 传入的类型实参必须是指定类型或其子类
public class Generic<? extends Number> {
    private ? key;

    public Generic(? key) {
        this.key = key;
    }

    public ? getKey(){
        return key;
    }
}

// 在泛型方法中添加上下边界限制的时候，必须在权限声明与返回值之间的<T>上添加上下边界，即在泛型声明的时候添加
// 比如：public <T> T showKeyName(Generic<T extends Number> container)，编译器会报错："Unexpected bound"
public <T extends Number> T showKeyName(Generic<T> container) {
    T test = container.getKey();
    return test;
}
```

#### 泛型数组

在 Java 中是不能创建一个确切的泛型类型的数组的。

```java
// 下面的这个例子是不可以的
List<String>[] ls = new ArrayList<String>[10];
// 而使用通配符创建泛型数组是可以的
List<?>[] ls = new ArrayList<?>[10];
// 这样也是可以的
List<String>[] ls = new ArrayList[10];

// 下面使用一个例子来说明这个问题
// Not really allowed：
List<String>[] lsa = new List<String>[10]; 
Object o = lsa;    
Object[] oa = (Object[]) o;    
List<Integer> li = new ArrayList<Integer>();    
li.add(new Integer(3));

// Unsound, but passes run time store check：
oa[1] = li; 

// Run-time error: ClassCastException：
String s = lsa[1].get(0);
```

这种情况下，由于 JVM 的泛型擦除机制，在运行时 JVM 是不知道泛型信息的，所以可以给 `oa[1]` 赋上一个 `ArrayList` 而不会出现异常，但是在取出数据的时候却要做一次类型转换，所以就会出现 `ClassCastException`，如果可以进行泛型数组的声明，上面说的这种情况在编译期将不会出现任何的警告和错误，只有在运行时才会出错。

而对泛型数组的声明进行限制，对于这样的情况，可以在编译期提示代码有类型安全问题，比没有任何提示要强很多。

采用通配符的方式是被允许的：数组的类型不可以是类型变量，除非是采用通配符的方式，因为对于通配符的方式，最后取出数据是要做显式的类型转换的。

> 参考资料：
>
> - [java 泛型详解-绝对是对泛型方法讲解最详细的，没有之一_VieLei的博客-CSDN博客_泛型](https://blog.csdn.net/s10461/article/details/53941091)

#### 多态参数

如果方法的参数是 Animal 类型的数组，那么它也能够传入其子类的数组（多态），但是泛型却不能直接使用多态。

示例：

```java
public void go() {
    // 1.多态参数
    Animal[] animals = {new Dog(), new Cat(), new Dog()};
    takeAnimals(animals); // 可以
    
    Dog[] dogs = {new Dog(), new Dog(), new Dog()};
    takeAnimals(dogs); // 可以
    
    // 2.泛型
    ArrayList<Animal> animals = new ArrayList<>();
    animals.add(new Dog());
    animals.add(new Cat());
    animals.add(new Dog());
    takeAnimals(animals); // 可以
    
    ArrayList<Dog> dogs = new ArrayList<>();
    animals.add(new Dog());
    animals.add(new Dog());
    takeAnimals(dogs); // 不可以
}

// 这里能够传入Animal[]和Dog[]类型的参数（多态）
public void takeAnimals(Animal[] animals) {
    for(Animal a : animals) {
        a.eat();
    }
    // 这里可能将一个Cat放入到Dog类型的数组中，但是数组的类型检查发生在运行期，而泛型在编译期
    // 因此下面的代码在编译时没有问题，到程序运行后才会报错
    aniamls[0] = new Cat(); 
}

// 如果将方法参数声明成ArrayList<Animal>，那么就只能传入这种类型的参数，在这里不能使用多态
public void takeAnimals(ArrayList<Animal> animals) {
    for(Animal a : animals) {
        a.eat();
    }
    // 不能使用多态是因为可能会出现如下的代码，此时如果传入的是一个ArrayList<Dog>，就出现了问题
    animals.add(new Cat());
}
```

如何解决？（通配符）

示例：

```java
// 这里的extends同时代表继承和实现
public void takeAnimals(ArrayList<? extends Animal> animals) {
    for(Animal a : animals) {
        a.eat();
    }
    // 在使用带有<?>的声明时，编译器不会让你加入任何东西到集合中
    // animals.add(new Cat());
    // 你能够调用list中任何元素的方法，但不能加入元素
}

// 下面两种方法的声明等价
public void func(ArrayList<? extends Animal> list);
public <T extends Animal> void func(ArrayList<T> list);

// 如何选择？
public void func(ArrayList<? extends Animal> one, ArrayList<? extends Animal> two);
// 下面这种方式更好，只用声明一次泛型
public <T extends Animal> void func(ArrayList<T> one, ArrayList<T> two);
```

#### 常见问题

##### （1）当泛型遇到重载

下面这段代码，有两个重载的函数，因为他们的泛型参数类型不同，一个是 String 另一个是 Integer，但是，这段代码是编译通不过的。因为泛型在编译之后会被擦除，变成相同的原生类型 List，擦除动作导致这两个方法的特征签名变得一模一样。

```java
public class GenericTypes {
    public static void method(List<String> list) {
        // ...
    }  
    public static void method(List<Integer> list) {  
        // ...
    }  
}
```

##### （2）当泛型遇到 catch

泛型的类型参数不能用在 Java 异常处理的 catch 语句中。因为异常处理是由 JVM 在运行时刻来进行的。由于类型信息被擦除，JVM 是无法区分两个异常类型 `MyException<String>` 和 `MyException<Integer>` 的。

##### （3）当泛型内包含静态变量

由于经过类型擦除，所有的泛型类实例都关联到同一份字节码上，因此泛型类的所有静态变量是共享的。

```java
public class StaticTest {
    public static void main(String[] args){
        GT<Integer> gti = new GT<Integer>();
        gti.var = 1;
        GT<String> gts = new GT<String>();
        gts.var = 2;
        System.out.println(gti.var); // 2
    }
}

class GT<T> {
    public static int var = 0;
    public void nothing(T x){}
}
```

### 排序

#### sort() 方法

定义：

```java
// java.util.Collections
public static <T extends Comparable<? super T>> void sort(List<T> list) {
    // ...
}
```

注意：

- 从方法的定义中可以看出，sort() 方法只能接受 Comparable 对象的 list。
- 对泛型来说，extends 代表“是一个”，同时适用于类和接口，即 extends 既可以表示继承，也可以表示实现（implements）。
- extends 表示子类或其自己（即 <=）。

#### Comparable 接口

定义：

```java
public interface Comparable<T> {
	int compareTo(T o);
}

int res = o1.compareTo(T o2);
// res > 0，代表 o1 < o2
// res < 0，代表 o1 > o2
// res = 0，代表 o1 = o2（并不代表两个对象真的相等）
```

示例：

```java
class Song implements Comparable<Song> {
    
    String title;
    String artist;
    
    @Override
    public int compareTo(Song s) {
        // 使用了 String 类的 compareTo() 方法
        return title.compareTo(s.getTitle());
    }
    
    // ... 
}
```

这样做的问题：只能按一种标准进行排序，不够灵活。

解决：使用比较器！

#### Comparator 比较器

定义：

```java
public interface Comparator<T> {
    int compare(T o1, T o2);
}
```

使用带有 Comparator 参数的 sort() 方法会按照比较器的规则进行排序，而忽略掉本来的 Comparable 中的排序规则。此时 list 中的元素不需要实现 Comparable 接口。

```java
class Song {
    
    String title;
    String artist;
    
    // ... 
}

class ArtistComparator implements Comparator<Song> {
    @Override
    public int compare(Song s1, Song s2) {
        // 使用了 String 类的 compareTo() 方法
        return s1.getArtist().compareTo(s2.getArtist());
    }
}

// sort(List o, Comparator c);
sort(songs, new ArtistComparator());
```

### 遍历

#### 增强 for 循环

for-each（增强 for）的实现原理其实就是使用了普通的 for 循环和迭代器。

错误情况：

```java
for (Student stu : students) {    
    if (stu.getId() == 2)     
        students.remove(stu);    
}
```

Iterator 工作在一个独立的线程中，并且拥有一个 mutex 锁。Iterator 被创建之后会建立一个指向原来对象的单链索引表，当原来的对象数量发生变化时，这个索引表的内容不会同步改变，所以当索引指针往后移动的时候就找不到要迭代的对象，然后会抛出 `java.util.ConcurrentModificationException` 异常。

所以 Iterator 在工作的时候是不允许被迭代的对象被改变的。但你可以使用 Iterator 本身的方法 remove() 来删除对象，`Iterator.remove()` 方法会在删除当前迭代对象的同时维护索引的一致性。

## 多线程

## 网络编程

## Java 8 新特性

### Lambda 表达式

#### 基本概念

- ->：lambda 操作符、箭头操作符；
- -> 左边：lambda 形参列表，是接口中抽象方法的形参列表；
- -> 右边：lambda 体，是重写的抽象方法的方法体。

示例：

```java
// 示例一
// 1.正常写法
Runnable r = new Runnable() {
	@Override
	public void run() {
		System.out.println("...");
	}
}
// 2.Lambda 表达式
Runnable r = () -> System.out.println("...");

// 示例二
// 1.正常写法
Comparator<Integer> com = new Comparator<Integer> {
    @Override
    public int compare(Integer o1, Integer o1) {
        return Integer.compare(o1, o2);
    }
}
// 2.Lambda 表达式
Comparator<Integer> com = (o1, o2) -> Integer.compare(o1, o2);
// 3.方法引用
Comparator<Integer> com = Integer::compare;
```

#### 使用方法

- 无参，无返回值：`() -> {重写的方法体};`
- 一个参数，无返回值：`(参数类型 形参) -> {重写的方法体};`
- 参数类型可以省略（当抽象方法的参数类型为接口中的泛型时，参数的类型可由编译器自动推断）：`(形参) -> {重写的方法体};`
- 若抽象方法只有一个参数，则参数的小括号可以省略：`形参 -> {重写的方法体};`
- 抽象方法有多个参数，多条执行语句，并且有返回值：`(o1, o2) -> {return o1.compareTo(o2)};`
- 当 lambda 体只有一条语句时，return 与大括号都可以省略：`(o1, o2) -> o1.compareTo(o2);`

### 函数式接口

#### 基本概念

lambda 表达式的本质：作为函数式接口的实例（对象）。

函数式接口：

- 接口中仅有一个抽象方法，不存在有多个抽象方法需要被重写的情况；
- 使用 `@FunctionalInterface` 声明一个函数接口；
- lambda 表达式只能依赖于函数式接口。

可以用 lambda 表达式来替代匿名实现类。

#### 常用接口

在 `java.util.function` 包下定义了 Java 8 的丰富的函数式接口：

![image-20220731164226318](Java%E7%9F%A5%E8%AF%86%E7%82%B9%E6%95%B4%E7%90%86.assets/image-20220731164226318.png)

### 方法引用

#### 基本概念

格式：使用 `::` 将类或对象与方法名分隔开。

- 当要传递给 lambda 体的操作，已经有实现的方法了，可以使用方法引用；
- 方法引用本质上还是一个 lambda 表达式，是函数式接口的一个实例，是通过方法的名字来指向一个方法；
- 前提：实现接口的抽象方法的参数列表和返回值类型，必须与方法引用的方法的参数列表和返回值类型一致。

#### 使用方法

- 对象::非静态方法
- 类::静态方法
- 类::非静态方法

示例：

```java
// 情况一：对象::非静态方法
// 示例一：
// Consumer 中的 void accept(T t)
// PrintStream 中的 void println(T t)，该方法已经实现了，可以直接使用
// 1.lambda 表达式
Consumer<String> con = str -> System.out.println(str);
con.accept(“北京”);
// 2.方法引用
PrintStream ps = System.out;
Consumer<String> con = ps::println;
con.accept(“北京”);
// 示例二：
// Supplier 中的 T get()
// Employee 中的 String getName()
Employee emp = new Employee(...);
// 1.lambda 表达式
Supplier<String> sup = () -> emp.getName();
System.out.println(sup.get());
// 2.方法引用
Supplier<String> sup = emp::getName();
System.out.println(sup.get());

// 情况二：类::静态方法
// Comparator 中的 int compare(T t1, T t2)
// Integer 中的 int compare(T t1, T t2)
// 1.lambda 表达式
Comparator<Integer> com = (o1, o2) -> Integer.compare(o1, o2);
System.out.println(com.compare(a, b));
// 2.方法引用
Comparator<Integer> com = Integer::compare;
System.out.println(com.compare(a, b));

// 情况三：类::非静态方法
// Comparator 中的 int compare(T t1, T t2)
// String 中的 int t1.compareTo(t2)
// 1.lambda 表达式
Comparator<String> com = (s1, s2) -> s1.compareTo(s2);
System.out.println(com.compare("a", "b"));
// 2.方法引用
Comparator<String> com = String::compareTo;
System.out.println(com.compare("a", "b"));
```

#### 构造器引用

……

#### 数组引用

……

### Stream API

#### 基本概念

Stream 是 Java 8 中处理集合的关键抽象概念，可以指定对集合进行的操作，可以执行非常复杂的查找、过滤和映射数据等操作。使用 Stream API 对集合数据进行操作，就类似于使用 SQL 执行的数据库查询。也可以使用它来并行执行操作。

Stream 是数据渠道，用于操作数据源（集合、数组等）所生成的元素列表。

Stream 和 Collection 集合的区别：

- Stream 关注的是对数据的运算，主要是面向 CPU，通过 CPU 实现计算；
- Collection 关注的是数据的存储，是一种静态的内存数据结构，主要是面向内存，数据存储在内存中。

Stream 的特点：

- Stream 自己不会存储元素；
- Stream 不会改变源对象，相反他们会返回一个新的 Stream；
- Stream 操作是延迟执行的，这代表他们会等到需要结果的时候才执行。

Stream 的执行流程：

- Stream 的实例化；
- 一系列的中间操作（过滤、映射、……）；
- 终止操作。

![image-20220731231145682](Java%E7%9F%A5%E8%AF%86%E7%82%B9%E6%95%B4%E7%90%86.assets/image-20220731231145682.png)

> 注意：延迟执行是指，只有当执行终止操作后，中间操作链上的一系列操作才会真的被执行，并生成结果。
>

#### 创建方式

##### （1）通过集合

Java 8 中的 Collection 接口被扩展，提供了两个获取流的方法（是接口的默认方法，需要使用接口实现类的对象去调用）：

- `default Stream<E> stream()`：返回一个顺序流（按顺序去取集合中的元素）；
- `default Stream<E> parallelStream()`：返回一个并行流（相当于多线程并行地去取集合中的元素，顺序就不一定能保证了）。

示例：

```java
List<Employee> employees = ...;
// 1.顺序流
Stream<Employee> stream = employees.stream();
// 2.并行流
Stream<Employee> parallelStream = employees.parallelStream();
```

##### （2）通过数组

Java 8 中的 Arrays 的静态方法 `stream()` 可以获取数组流：

- `static <T> Stream<T> stream(T[] array)`：返回一个流。

对应的重载形式：

![image-20220731232957068](Java%E7%9F%A5%E8%AF%86%E7%82%B9%E6%95%B4%E7%90%86.assets/image-20220731232957068.png)

示例：

```java
// 1.int类型数组
int[] arr = ...;
IntStream stream = Arrays.stream(arr);
// 2.自定义类型数组
Employee[] arr = ...;
Stream<Employee> stream = Arrays.stream(arr);
```

##### （3）通过 Stream 的 of() 方法

可以调用 Stream 类的静态方法 `of()`，通过显示值创建一个流，它可以接收任意数量的参数：

- `public static <T> Stream<T> of(T... values)`：返回一个流。

示例：

```java
Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6);
```

##### （4）创建无限流（使用较少）

可以使用静态方法 Stream.iterate() 和 Stream.generate() 创建无限流：

- `public static <T> Stream<T> iterate(...)`：迭代。
- `public static <T> Stream<T> generate(...)`：生成。

#### 中间操作

多个中间操作可以连接起来形成一个流水线，除非流水线上触发终止操作，否则中间操作不会执行任何的处理。

> 惰性求值：在终止操作时一次性全部处理。
>

##### （1）筛选与切片

常用方法：

|        方法         |                             描述                             |
| :-----------------: | :----------------------------------------------------------: |
| filter(Predicate p) |               接收 Lambda，从流中排除某些元素                |
|     distinct()      | 筛选，通过流所生成元素的 hashCode() 和 equals() 去除重复元素 |
| limit(long maxSize) |                截断流，使其元素不超过给定数量                |
|    skip(long n)     | 跳过元素，返回一个扔掉了前 n 个元素的流，若流中元素不足 n 个，则返回一个空流（与 limit(n) 互补） |

示例：

```java
// 1.过滤
List<Employee> list = ...;
Stream<Employee> stream = list.stream();
// filter()方法的定义语句用了Stream类的泛型，所以下面可以直接写e
// 使用终止语句forEach()获得结果，传入参数为消费者
stream.filter(e -> e.getSalary() > 7000).forEach(System.out::println);

// 2.截断流
// 因为之前已经使用了终止语句，因此之前的流已经消失了，不能再回到中间操作继续执行，需要从集合获取新的流
// 只获取前3个
list.stream().limit(3).forEach(System.out::println);

// 3.跳过
// 跳过前3个，只获取后面的
list.stream().skip(3).forEach(System.out::println);

// 4.筛选（去重）
list.stream().distinct().forEach(System.out::println);
```

##### （2）映射

常用方法：

|        方法         |                             描述                             |
| :-----------------: | :----------------------------------------------------------: |
|   map(Function f)   | 接收一个函数作为参数，将元素转换成其它形式或提取信息，该函数会被应用到每个元素上，并将其映射成一个新的元素 |
| flatMap(Function f) | 接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流 |

示例：

```java
// 1.map
List<String> list = Arrays.asList("aa", "bb", "cc");
// 本质上就是以Stream的元素作为参数，传入map里的函数式接口的重写方法，再用返回值替换元素
list.stream().map(str -> str.toUpperCase()).
              forEach(System.out::println);
// 练习：获取员工姓名长度大于3的员工的姓名
List<Employee> employees = ...;
Stream<String> stream = employees.stream();
stream.map(Employee::getName).
       filter(name -> name.length() > 3).
       forEach(System.out::println);

// 2.flatMap
// “流中流”：将小流合并为大流
// （1）map类似于list的add()方法
// （2）flatMap类似于list的addAll()方法，将list2中的每个元素加入到原list中，形成一个新的list
ArrayList list1 = new ArrayList();
list1.add(1);
list1.add(2);
list1.add(3);
ArrayList list2 = new ArrayList();
list2.add(4);
list2.add(5);
list2.add(6);
list1.add(list2); // {1, 2, 3, {4, 5, 6}}
list1.addAll(list2); // {1, 2, 3, 4, 5, 6}
// 将字符串中的多个字符构成的集合转换为对应的Stream实例
public Stream<Character> fromStringToStream(String str){
    ArrayList<Character> list = new ArrayList<>();
    for(Character c : str.toCharArray()){
        list.add(c);
    }
    return list.stream();
}
// 方式一：用map实现（比较复杂）
Stream<Stream<Character>> streamStream = list.stream().map(当前类名::fromStringToStream);
streamStream.forEach(s -> {
    s.forEach(System.out::println); // 输出结果：a a b b c c（合并为了一个流）
});
// 方式二：使用flatMap实现
Stream<Stream<Character>> characterStream = list.stream().flatMap(当前类名::fromStringToStream);
characterStream.forEach(System.out::println); // 输出结果同上
```

##### （3）排序

常用方法：

|          方法          |              描述              |
| :--------------------: | :----------------------------: |
|        sorted()        |  产生一个新流，按自然顺序排序  |
| sorted(Comparator com) | 产生一个新流，用比较器顺序排序 |

示例：

```java
// 1.自然排序
List<Integer> list = Arrays.asList(12, 43, 65, ...);
list.stream().sorted().forEach(System.out::println);

// 2.比较器排序
List<Employee> employees = ...;
employees.stream().sorted((e1, e2) -> { 
	int ageValue = Integer.compare(e1.getAge(), e2.getAge());
    if(ageValue != 0) {
        return value;
    } else {
        return Double.compare(e1.getSalary(), e2.getSalary());
    }
}).forEach(System.out::println);
```

#### 终止操作

执行终止操作会从流的流水线生成结果，其结果可以是任何不是流的值，例如：List、Integer，甚至是 void。

##### （1）匹配与查找

常用方法：

|          方法          |           描述           |
| :--------------------: | :----------------------: |
| allMatch(Predicate p)  |   检查是否匹配所有元素   |
| anyMatch(Predicate p)  | 检查是否匹配至少一个元素 |
| noneMatch(Predicate p) | 检查每个元素是否都不匹配 |
|      findFirst()       |      返回第一个元素      |
|       findAny()        |  返回当前流中的任意元素  |
|        count()         |   返回流中元素的总个数   |
|   max(Comparator c)    |      返回流中最大值      |
|   min(Comparator c)    |      返回流中最小值      |
|  forEach(Consumer c)   |         内部迭代         |

示例：

```java
// 1.allMatch
// 检查是否所有员工的年龄都大于18
List<Employee> employees = ...;
boolean allMatch = employees.stream().allMatch(e -> e.getAge() > 18);
System.out.println(allMatch);

// 2.anyMatch
// 检查是否存在员工的薪资大于10000
boolean anyMatch = employees.stream().anyMatch(e -> e.getSalary() > 10000);
System.out.println(anyMatch);

// 3.noneMatch
// 检查是否没有员工姓“雷”
boolean noneMatch = employees.stream().noneMatch(e -> e.getAge().startWith("雷"));
System.out.println(noneMatch);

// 4.findFirst
Optional<Employee> employee = employees.stream().findFirst();
System.out.println(employee);

// 5.findAny
Optional<Employee> employee = employees.stream().findAny();
System.out.println(employee);

// 6.count
long count = employees.stream().filter(e -> e.getSalary() > 5000).count();
System.out.println(count);

// 7.max
// 返回员工最高的工资
Stream<Double> salaryStream = employees.stream().map(e -> e.getSalary());
Optional<Double> maxSalary = salaryStream.max(Double::compare);
System.out.println(maxSalary);

// 8.min
// 返回工资最低的员工
Optional<Employee> employee = employees.stream().min((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary()));
System.out.println(employee);

// 9.forEach
employees.stream().forEach(System.out::println);
```

##### （2）归约

常用方法：

|               方法               |                            描述                            |
| :------------------------------: | :--------------------------------------------------------: |
| reduce(T iden, BinaryOperator b) |      可以将流中元素反复结合起来，得到一个值，返回 `T`      |
|     reduce(BinaryOperator b)     | 可以将流中元素反复结合起来，得到一个值，返回 `Optional<T>` |

> BinaryOperator 继承自二元函数，它需要两个参数 a 和 b，返回一个参数 c，且 a、b、c 的类型都相同。
>

示例：

```java
// 1.reduce(T iden, BinaryOperator b)
// 计算1-10的自然数的和
List<Integer> list = Arrays.asList(1, 2, ... , 10);
// 这里的0代表初始值
Integer sum = list.stream().reduce(0, Integer::sum);
System.out.println(sum); // 55

// 2.reduce(BinaryOperator b)
// 计算所有员工工资的总和
List<Employee> employees = ...;
Stream<Double> salaryStream = employees.stream().map(e -> e.getSalary());
Optional<Double> sumMoney = salaryStream.reduce(Double::sum);
// 或者：Optional<Double> sumMoney = salaryStream.reduce((d1, d2) -> d1 + d2);
System.out.println(sumMoney);
```

> map + reduce 的连接模式通常被称为 map-reduce 模式。

##### （3）收集

常用方法：

|         方法         |                             描述                             |
| :------------------: | :----------------------------------------------------------: |
| collect(Collector c) | 将流转换为其它形式，接收一个 Collector 接口的实现，用于将 Stream 中的所有元素进行汇总 |

Collector API：

|     方法     |    返回类型     |             作用              |
| :----------: | :-------------: | :---------------------------: |
|    toList    |    `List<T>`    |    将流中的元素收集到 List    |
|    toSet     |    `Set<T>`     |    将流中的元素收集到 Set     |
| toCollection | `Collection<T>` | 将流中的元素收集到 Collection |

示例：

```java
List<Employee> employees = ...;
List<Employee> employeeList = employees.stream().filter(e -> e.getSalary() > 6000).collect(Collectors.toList());
employeeList.forEach(System.out::println);
```

#### 参考资料

- [Stream流式编程，让代码变优雅 (qq.com)](https://mp.weixin.qq.com/s/QuajvAZAQ9J690xMMl_g7w)
- [20 个实例玩转 Java 8 Stream (qq.com)](https://mp.weixin.qq.com/s/qQHMbaoF5EXNuhuku-m09Q)
- [面试官：你天天用 Stream，那你知道 Stream 的实现原理吗？ (qq.com)](https://mp.weixin.qq.com/s/ohBCnz0G3I-SKZcFd4KMAQ)

### Optional 类

#### 基本概念

`Optional<T>` 类（`java.util.Optional`）是一个容器类，它可以保存类型 T 的值，代表这个值存在，或者仅仅保存 null，表示这个值不存在。原来用 null 表示一个值不存在，现在 Optional 可以更好的表达这个概念，并且可以避免空指针异常。

这是一个可以为 null 的容器对象，如果值存在则 `isPresent()` 会返回 true，调用 `get()` 方法会返回该对象。（不用显示进行空值检查）

#### 常用方法

![image-20220801171115971](Java%E7%9F%A5%E8%AF%86%E7%82%B9%E6%95%B4%E7%90%86.assets/image-20220801171115971.png)

示例：

```java
A a = new A();
Optional<A> optionalA = optional.of(a); // 正确

A a = null;
Optional<A> optionalA = optional.of(a); // 报错，空指针异常
Optional<A> optionalA = optional.ofNullable(a); // 正确，输出：Optional.empty

// 若optionalA不为空，则直接返回
// 若optionalA为空，则返回new A()进行替代
optionalA.orElse(new A());
```

#### 使用实例

```java
class Boy {
    String name;
    Girl girl;
    // ...
}

class Girl {
    String name;
    String getName() {...}
    // ...
}

// 传统方式：为了避免出现空指针异常，需要层层进行判断
public String getGirlName2(Boy boy) {
    if(boy != null) {
        Girl girl = boy.getGirl();
        if(girl != null) {
            return girl.getName();
        }
    }
}

// 使用 Optional 优化
public String getGirlName2(Boy boy) {
    Optional<Boy> boyOptional = Optional.ofNullable(boy);
    Boy boy1 = boyOptional.orElse(new Boy(new Girl("备胎")));
    // 这里可以保证boy1一定不为null
    Girl girl = boy1.getGirl();
    Optional<Girl> girlOptional = Optional.ofNullable(girl);
    Girl girl1 = girlOptional.orElse(new Girl("备胎"));
    // 这里可以保证girl1一定不为null
    return girl.getName();
}
```

> 注意：在实际开发中一般只会包装一层，本例中使用得略显复杂。
>

### 新日期时间 API

### 接口默认方法
