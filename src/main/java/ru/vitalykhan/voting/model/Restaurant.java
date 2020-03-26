package ru.vitalykhan.voting.model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.Map;

@Entity
public class Restaurant extends AbstractNamedEntity {

    @OneToMany
    private Map<LocalDate, Menu> menu;

    public Restaurant() {
    }

    public Restaurant(Integer id, String name, Map<LocalDate, Menu> menu) {
        super(id, name);
        this.menu = menu;
    }

    public Map<LocalDate, Menu> getMenu() {
        return menu;
    }

    public void setMenu(Map<LocalDate, Menu> menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", menu=" + menu +
                '}';
    }
}
