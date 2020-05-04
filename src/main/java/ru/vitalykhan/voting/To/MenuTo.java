package ru.vitalykhan.voting.To;

import java.time.LocalDate;

public class MenuTo extends AbstractTo {
    private LocalDate date;

    public MenuTo() {
    }

    public MenuTo(LocalDate date) {
        this.date = date;
    }

    public MenuTo(Integer id, LocalDate date) {
        super(id);
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MenuTo{" +
                "date=" + date +
                ", id=" + id +
                '}';
    }
}
