package com.corp.listener;

import com.corp.entity.Audit;
import org.hibernate.event.spi.*;

import java.io.Serializable;

public class AuditTableListener implements PreDeleteEventListener, PreInsertEventListener {

    @Override
    public boolean onPreDelete(PreDeleteEvent event) {
        auditEntity(event, Audit.Operation.DELETE);
        return false;
    }

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        auditEntity(event, Audit.Operation.INSERT);
        return false;
    }

    private void auditEntity(AbstractPreDatabaseOperationEvent event, Audit.Operation operation) {
        if (event.getEntity().getClass() != Audit.class) {
            Audit audit = Audit.builder()
                    .entityId((Serializable) event.getId())
                    .entityName(event.getClass().getName())
                    .entityContent(event.getEntity().toString())
                    .operation(operation)
                    .build();
            event.getSession().persist(audit);
        }
    }
}
