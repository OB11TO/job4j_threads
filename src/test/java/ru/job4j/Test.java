package ru.job4j;

public class Test {
    public static final String BBB = "FF";

    public static void main(String[] args) {
//        methodDemoWithPolimorhizm();
//        int a = 4324;
//        float b = a;
//        OuterClass.StaticInnerClass inner = new OuterClass.StaticInnerClass();
//        inner.printStaticMember(); // Вывод: I am static
//        procentOtDelete();

    }

    private static void procentOtDelete() {
        int[] ints = {5, 7, 8, 2, 1};
        int number = 2;
        // i / number = wholePart
        // wholePart * number = middlePart
        // i - middePart = remainderOfDivision
        int wholePart = 0;
        int middlePart = 0;
        int remainderOfDivision = 0;
        int[] remainderOfDivisionsArray = new int[ints.length];
        for (int i = 0; i < ints.length - 1; i++) {
            wholePart = ints[i] / number;
            middlePart = wholePart * number;
            remainderOfDivision = ints[i] - middlePart;
            remainderOfDivisionsArray[i] = remainderOfDivision;
        }
        System.out.println();
    }

    private static void methodDemoWithPolimorhizm() {
        Whale whale = new Whale();
        whale.printAll(); //Я – белая Я – кит

        Cow cow = new Whale();
        cow.printName(); // Я кит

        Whale whale2 = new Whale();
        whale2.printName();
        whale2.printColor();
    }
}

class Cow {
    public void printAll() {
        printColor();
        printName();
    }

    public void printColor() {
        System.out.println("Я - белая");
    }

    public void printName() {
        System.out.println("Я - корова");
    }
}

class Whale extends Cow {
    public void getName() {
        System.out.println("<UNK> - <UNK>");
    }

    public void printName() {
        System.out.println("Я - кит");
    }
}

class City {

    class Airport {
        public Airport() {
            System.out.println("Airport создан");
        }
    }
}

class Miniport extends City.Airport {
    public Miniport(City city) {
        city.super(); // привязка к конкретному city
        System.out.println("Miniport создан");
    }
}

class OuterClass {
    private static String staticMember = "I am static";

    static class StaticInnerClass {
        public void printStaticMember() {
            // Доступ к статическим членам внешнего класса
            System.out.println(staticMember);
        }
    }
}
