package sample;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="BETA_TABLE")
public class Beta implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;

    public Beta(String value) {
        this.code = value;
    }

    @Override
    public String toString() {
        return "Beta{" +
                "id=" + id +
                ", code='" + code + '\'' +
                '}';
    }

    @Deprecated
    protected Beta() {}
}
