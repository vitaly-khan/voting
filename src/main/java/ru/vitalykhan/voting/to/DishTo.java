package ru.vitalykhan.voting.to;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

public class DishTo extends AbstractTo {
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    //"Zero", because complimentary dish can be possible use case
    @PositiveOrZero
    private int price;      //price multiplied by 100

    @Positive
    private int menuId;

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Integer getMenuId() {
        return menuId;
    }

    @Override
    public String toString() {
        return "DishTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", menuId=" + menuId +
                '}';
    }
}
