package items.items.infrastructure.persistence.item;

import items.items.domain.model.RoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.envers.Audited;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "item_permissions")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemPermissionEntity {

  @Id
  @Column(length = 36)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id", nullable = false)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  private ItemEntity item;

  @Column(name = "user_id", nullable = false, length = 36)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  private UUID userId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private RoleEnum role;

}