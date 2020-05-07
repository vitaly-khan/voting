package ru.vitalykhan.voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Entity
public class Menu extends AbstractEntityWithId {

    @NotNull
    @FutureOrPresent            //Creating backdated menus is not allowed.
    private LocalDate date;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
    @OneToMany
    //List is preferred over Set as it's likely to set an order of dishes within menu in perspective
    private List<Dish> dishes = Collections.emptyList();

    public Menu() {
    }

    public Menu(LocalDate date, Restaurant restaurant) {
        this.date = date;
        this.restaurant = restaurant;
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
