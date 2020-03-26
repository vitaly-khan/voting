package ru.vitalykhan.voting.model;

import java.time.LocalDate;
import java.util.List;

public class Menu extends AbstractEntityWithId {

    private LocalDate date;

    //List is preferred over Set as it's likely to set an order of dishes within menu in future
    private List<Dish> dishes;

    public Menu() {
    }

    public Menu(Integer id, LocalDate date, List<Dish> dishes) {
        super(id);
        this.date = date;
        this.dishes = dishes;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", date=" + date +
                ", dishes=" + dishes +
                '}';
    }
}
