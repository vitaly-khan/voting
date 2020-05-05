package ru.vitalykhan.voting.to;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

public class DishTo extends AbstractTo {
    @NotBlank
    @Size(min = 2, max = 100)
    protected String name;

    //"Zero", because complimentary dish can be possible use case
    @PositiveOrZero
    private Integer price;      //price multiplied by 100

    @Positive
    private Integer menuId;

    public DishTo() {
    }

    public DishTo(String name, Integer price, Integer menuId) {
        this.name = name;
        this.price = price;
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "DishTo{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", menuId=" + menuId +
                '}';
    }
}
