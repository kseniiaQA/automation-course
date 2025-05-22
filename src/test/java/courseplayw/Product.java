package courseplayw;

public class Product {
    public Product() {
        super();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    private String name;

    // Конструктор
    public Product(String name) {
        this.name = name;
    }

    // Геттер для имени продукта
    public String getName() {
        return name;
    }


}
