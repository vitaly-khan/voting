package ru.vitalykhan.voting.To;

public class AbstractTo {
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
