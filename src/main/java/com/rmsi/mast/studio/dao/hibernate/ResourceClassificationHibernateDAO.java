package com.rmsi.mast.studio.dao.hibernate;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rmsi.mast.studio.dao.ResourceClassificationDAO;
import com.rmsi.mast.studio.domain.ResourceClassification;
import java.util.List;

@Repository
public class ResourceClassificationHibernateDAO extends GenericHibernateDAO<ResourceClassification, Integer> implements ResourceClassificationDAO {

    @Override
    public ResourceClassification getById(Integer Id) {
        Query query = getEntityManager().createQuery("Select d from ResourceClassification d where d.classificationid = :id");
        ResourceClassification editAttrib = (ResourceClassification) query.setParameter("id", Id).getSingleResult();
        if (editAttrib != null) {

            return editAttrib;
        } else {
            return null;
        }
    }
    
    @Override
    public List<ResourceClassification> getActiveClassifications(){
        Query query = getEntityManager().createQuery("Select c from ResourceClassification c where c.isactive=true");
        return query.getResultList();
    }
}
