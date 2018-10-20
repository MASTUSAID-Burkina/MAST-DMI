package com.rmsi.mast.viewer.dao;

import com.rmsi.mast.studio.dao.GenericDAO;
import com.rmsi.mast.studio.domain.Permission;

public interface PermissionDao extends GenericDAO<Permission, Integer> {
    Permission findById(int id);
    Permission findRegisteredPermissionByPropId(long usin);
    Permission findPendingPermissionByPropId(long usin);
    Permission findPermissionByTransactionId(int transactionId);
    Permission findPendingTerminationPermissionByPropId(long usin);
    Permission save(Permission permission);
    boolean checkForRegisteredPermission(long usin);
}
