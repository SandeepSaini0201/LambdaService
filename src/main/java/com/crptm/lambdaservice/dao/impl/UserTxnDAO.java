package com.crptm.lambdaservice.dao.impl;

import com.crptm.lambdaservice.client.MySQLClient;
import com.crptm.lambdaservice.constants.ErrorConstants;
import com.crptm.lambdaservice.constants.UserTxnEntityConstants;
import com.crptm.lambdaservice.dao.converter.UserTxnConverter;
import com.crptm.lambdaservice.dao.entity.UserTxnEntity;
import com.crptm.lambdaservice.dao.interfaces.IUserTxnDAO;
import com.crptm.lambdaservice.enums.EnumPaymentStatus;
import com.crptm.lambdaservice.exception.ErrorOpeningSessionException;
import com.crptm.lambdaservice.pojo.UserTxnPOJO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Repository
@Slf4j
public class UserTxnDAO implements IUserTxnDAO {

    @Autowired
    private UserTxnConverter userTxnConverter;

    @Autowired
    private MySQLClient mySQLClient;

    @Override
    public void updatePaymentStatusByStatusAndBeforeInstant(EnumPaymentStatus newPaymentStatus, EnumPaymentStatus oldPaymentStatus, Instant beforeInstant) {
        Session session = null;
        try {
            session = this.mySQLClient.getSessionFactory().openSession();
            this.updateStatusByStatusAndBeforeInstant(newPaymentStatus, oldPaymentStatus, beforeInstant, session);
            session.close();
        } catch (HibernateException e) {
            String errorMsg = ErrorConstants.ERROR_OPENING_HIBERNATE_SESSION;
            log.error(errorMsg, e);
            throw new ErrorOpeningSessionException(errorMsg);
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<UserTxnPOJO> findTxnsByPaymentStatusAndBeforeInstant(EnumPaymentStatus paymentStatus, Instant beforeInstant) {
        Session session = null;
        try {
            session = this.mySQLClient.getSessionFactory().openSession();
            List<UserTxnEntity> userTxnEntities = this.findByStatusAndBeforeInstant(paymentStatus, beforeInstant, session);
            session.close();
            return this.userTxnConverter.userTxnEntitiesToPOJOs(userTxnEntities);
        } catch (HibernateException e) {
            String errorMsg = ErrorConstants.ERROR_OPENING_HIBERNATE_SESSION;
            log.error(errorMsg, e);
            throw new ErrorOpeningSessionException(errorMsg);
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void updatePaymentStatusByTxnId(Long txnId, EnumPaymentStatus paymentStatus) {
        Session session = null;
        try {
            session = this.mySQLClient.getSessionFactory().openSession();
            this.updateStatusById(txnId, paymentStatus, session);
            session.close();
        } catch (HibernateException e) {
            String errorMsg = ErrorConstants.ERROR_OPENING_HIBERNATE_SESSION;
            log.error(errorMsg, e);
            throw new ErrorOpeningSessionException(errorMsg);
        } finally {
            if (session != null) session.close();
        }
    }

    private void updateStatusById(Long txnId, EnumPaymentStatus paymentStatus, Session session) {
        Transaction transaction = session.beginTransaction();
        Query<UserTxnEntity> query = session.createNamedQuery(UserTxnEntityConstants.UPDATE_STATUS_BY_ID, UserTxnEntity.class);
        query.setParameter("status", paymentStatus);
        query.setParameter("txnId", txnId);
        query.executeUpdate();
        transaction.commit();
    }

    private List<UserTxnEntity> findByStatusAndBeforeInstant(EnumPaymentStatus paymentStatus, Instant beforeInstant, Session session) {
        Query<UserTxnEntity> query = session.createNamedQuery(UserTxnEntityConstants.FIND_BY_STATUS_AND_BEFORE_INSTANT, UserTxnEntity.class);
        query.setParameter("status", paymentStatus);
        query.setParameter("instant", beforeInstant);
        List<UserTxnEntity> userTxnEntityList = query.list();
        if (userTxnEntityList == null) return Collections.emptyList();
        return userTxnEntityList;
    }

    private void updateStatusByStatusAndBeforeInstant(EnumPaymentStatus newPaymentStatus, EnumPaymentStatus oldPaymentStatus, Instant beforeInstant, Session session) {
        Transaction transaction = session.beginTransaction();
        Query<UserTxnEntity> query = session.createNamedQuery(UserTxnEntityConstants.UPDATE_STATUS_BY_STATUS_AND_BEFORE_INSTANT, UserTxnEntity.class);
        query.setParameter("newStatus", newPaymentStatus);
        query.setParameter("oldStatus", oldPaymentStatus);
        query.setParameter("instant", beforeInstant);
        query.executeUpdate();
        transaction.commit();
    }
}
