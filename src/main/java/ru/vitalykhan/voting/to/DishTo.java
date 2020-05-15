package ru.vitalykhan.voting.to;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

public class DishTo extends AbstractTo {

    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 100;

    @NotBlank
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
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
