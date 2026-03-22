package items.items.infrastructure.persistence.item;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "items")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity {

  @Id
  @Column(length = 36)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  private UUID id;

  @Column(name = "owner_id", nullable = false, length = 36)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  private UUID ownerId;

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String content;

  @Version
  @Column(nullable = false)
  @NotAudited
  private Integer version;

  @Column(nullable = false)
  private boolean deleted;

  @Column(name = "created_at", updatable = false)
  @CreationTimestamp
  @NotAudited
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  @NotAudited
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
  @Audited
  private List<ItemPermissionEntity> permissions = new ArrayList<>();

  public void addPermission(ItemPermissionEntity permission) {
    permissions.add(permission);
    permission.setItem(this);
  }

}