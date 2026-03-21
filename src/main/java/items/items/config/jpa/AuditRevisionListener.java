package items.items.config.jpa;

import items.items.config.security.AuthHelper;
import items.items.config.security.dto.AuthDto;
import org.hibernate.envers.RevisionListener;

public class AuditRevisionListener implements RevisionListener {

  @Override
  public void newRevision(Object entity) {
    AuditableRevisionEntity auditableRevisionEntity = (AuditableRevisionEntity) entity;

    AuthDto authDto = AuthHelper.getAuth();

    if (authDto != null && authDto.login() != null) {
      auditableRevisionEntity.setChangedBy(authDto.login());
    } else {
      auditableRevisionEntity.setChangedBy("SYSTEM");
    }
  }

}
