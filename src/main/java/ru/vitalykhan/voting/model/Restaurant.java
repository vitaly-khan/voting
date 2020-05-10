package ru.vitalykhan.voting.model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.Map;

@Entity
public class Restaurant extends AbstractNamedEntity {

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
