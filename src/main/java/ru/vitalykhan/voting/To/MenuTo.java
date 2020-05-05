package ru.vitalykhan.voting.To;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

public class MenuTo extends AbstractTo {
    @NotNull
    @FutureOrPresent            //Creating backdated menus is not allowed.
    private LocalDate date;

    @Positive
    private Integer restaurantId;

    public MenuTo() {
    }

    public MenuTo(LocalDate date, int restaurantId) {
        this.date = date;
        this.restaurantId = restaurantId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "MenuTo{" +
                "id=" + id +
                ", date=" + date +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
