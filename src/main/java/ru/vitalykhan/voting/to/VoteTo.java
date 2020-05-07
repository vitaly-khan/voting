package ru.vitalykhan.voting.to;

import javax.validation.constraints.Positive;

public class VoteTo extends AbstractTo {

    @Positive
    private Integer menuId;

    public VoteTo() {
    }

    public VoteTo(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "VoteTo{" +
                "id=" + id +
                ", menuId=" + menuId +
                '}';
    }
}
