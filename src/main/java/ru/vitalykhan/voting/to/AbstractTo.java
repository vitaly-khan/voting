package ru.vitalykhan.voting.to;

import ru.vitalykhan.voting.HasId;

public class AbstractTo implements HasId {
    protected Integer id;

    public AbstractTo() {
    }

    public AbstractTo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
