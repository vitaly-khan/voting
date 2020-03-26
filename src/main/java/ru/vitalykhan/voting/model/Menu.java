package ru.vitalykhan.voting.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Menu extends AbstractEntityWithId {

    @NotNull
    @FutureOrPresent
    private LocalDate date;

    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    //List is preferred over Set as it's likely to set an order of dishes within menu in future
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "menu")
    private List<Dish> dishes;

    public Menu() {
    }

    public Menu(Integer id, @NotNull LocalDate date, Restaurant restaurant, List<Dish> dishes) {
        super(id);
        this.date = date;
        this.restaurant = restaurant;
        this.dishes = dishes;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
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
