package com.example.demo.infra.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity extends Auditable {

  @Version
  @Builder.Default
  private int version = -1;
}
