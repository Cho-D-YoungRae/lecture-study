package study.datajpa2.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Item extends BaseTimeEntity implements Persistable<String>  {

    @Id
    private String id;

    @Override
    public boolean isNew() {
        return Objects.isNull(getCreatedDate());
    }
}
