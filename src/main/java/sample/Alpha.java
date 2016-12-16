package sample;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="ALPHA_TABLE", schema="PUBLIC")
public class Alpha implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;

    public Alpha(String value) {
        this.code = value;
    }

    @Override
    public String toString() {
        return "Alpha{" +
                "id=" + id +
                ", code='" + code + '\'' +
                '}';
    }

    @Deprecated
    protected Alpha() {}
}
