package ru.vitalykhan.voting.model;

import javax.persistence.Entity;

@Entity
public class Restaurant extends AbstractNamedEntity {

    private boolean enabled = true;

    public Restaurant() {
    }

    public Restaurant(String name) {
        super(null, name);
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    public Restaurant(Integer id, String name, boolean enabled) {
        super(id, name);
        this.enabled = enabled;
    }

    public Restaurant(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.enabled = restaurant.isEnabled();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
