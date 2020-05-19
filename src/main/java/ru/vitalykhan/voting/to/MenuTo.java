package ru.vitalykhan.voting.to;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

public class MenuTo extends AbstractTo {
    @NotNull
    private LocalDate date;

    @Positive
    private int restaurantId;

    public LocalDate getDate() {
        return date;
    }

    public int getRestaurantId() {
        return restaurantId;
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
