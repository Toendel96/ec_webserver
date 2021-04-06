package no.dnb.reskill.ec_webserver.models;

import lombok.*;

import javax.persistence.*;

// This is just a POJO, we create new instances of this class "just as normal"
// This will evolve into an Entity class (a class that corresponds to a table in a database)
// ORM => object-relational-mapping -> what to be done by JPA

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="Product")
public class Product {
    public static final int DUMMY_ID = -1;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) //This equals auto_increment
    @Getter private long id;
    private String name;
    @Enumerated(EnumType.ORDINAL)

    @Column(name="productcategory")
    private ProductCategory productCategory; //Hibernate vil automatisk omgjøre til product_category
    private double price;

    @Column(name="instock")
    private long inStock;


    public Product(String name, ProductCategory productCategory, double price, long inStock  ) {
        this(DUMMY_ID, name, productCategory, price, inStock);
    }

    public double adjustPriceByPercent(double percent) {
        return this.price *= 1 + percent/100;
    }

    @Override
    public String toString() {
        return String.format(
                "Product[id=%d, name='%s' (%s) price=%.2f inStock=%d]",
                id, name, productCategory, price, inStock);
    }

    // Important to help the entity manager merge objects with the same PK
    // Must have Object parameter since the original equals()-function has it
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Product) {
            Product o = (Product)other;
            result = this.id == o.id;
        }
        return result;
    }

    @Override
    public int hashCode() {
        return (int)id;
    }
}




