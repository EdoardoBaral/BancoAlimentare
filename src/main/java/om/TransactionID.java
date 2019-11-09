package om;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("TransactionID")
public class TransactionID
{
    @Id
    private String className;
    private Long id;

    public TransactionID()
    {
        className = "EntityRegistro";
        id = 1L;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
}
