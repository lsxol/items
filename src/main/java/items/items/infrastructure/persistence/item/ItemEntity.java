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
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
  private UUID id;

  @Column(name = "owner_id", nullable = false, length = 36)
  private UUID ownerId;

  @Column(nullable = false, length = 255)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String content;

  @Version // 3. Wymaganie: Optimistic locking! Hibernate sam podbije tę cyferkę przy update
  @Column(nullable = false)
  private Integer version;

  @Column(nullable = false)
  private boolean deleted;

  @Column(name = "created_at", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  @LastModifiedDate
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
  @Audited
  private List<ItemPermissionEntity> permissions = new ArrayList<>();

  public void addPermission(ItemPermissionEntity permission) {
    permissions.add(permission);
    permission.setItem(this);
  }

  public void removePermission(ItemPermissionEntity permission) {
    permissions.remove(permission);
    permission.setItem(null);
  }
}