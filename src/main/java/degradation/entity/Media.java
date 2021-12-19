package degradation.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public abstract class Media extends BaseEntity {

  @Column(unique = true, nullable = false)
  private String name;

  private Long size;
}
