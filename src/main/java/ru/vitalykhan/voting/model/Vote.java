package ru.vitalykhan.voting.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Vote extends AbstractEntityWithId {

    @NotNull
    @FutureOrPresent
    private LocalDate date;

    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Vote() {
    }

    public Vote(LocalDate date, Menu menu, User user) {
        this.date = date;
        this.menu = menu;
        this.user = user;
    }

    public Vote(Integer id, LocalDate date, Menu menu, User user) {
        super(id);
        this.date = date;
        this.menu = menu;
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ". date=" + date +
                ", menu=" + menu +
                ", user=" + user +
                '}';
    }
}
