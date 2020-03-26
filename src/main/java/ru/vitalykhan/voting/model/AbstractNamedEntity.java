package ru.vitalykhan.voting.model;

//import javax.persistence.Column;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Size;


//@MappedSuperclass
public abstract class AbstractNamedEntity extends AbstractEntityWithId {

//    @NotBlank
//    @Size(min = 2, max = 100)
//    @Column(name = "name", nullable = false)
    protected String name;

    protected AbstractNamedEntity() {
    }

    protected AbstractNamedEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return super.toString() + '(' + name + ')';
    }
}