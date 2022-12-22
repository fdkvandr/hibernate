package com.corp.listener;

import com.corp.entity.Revision;
import org.hibernate.envers.RevisionListener;

public class MyRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        // SecurityContext.getUser().getId()
        ((Revision) revisionEntity).setUsername("Andrey");
    }
}
