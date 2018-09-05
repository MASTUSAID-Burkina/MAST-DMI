package com.rmsi.mast.studio.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.rmsi.mast.studio.dao.ProjectNameDAO;
import com.rmsi.mast.studio.domain.ProjectName;

@Repository
public class ProjectNameHibernateDAO extends GenericHibernateDAO<ProjectName, Long> implements ProjectNameDAO {

}
