package com.holley.mvc.shiro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

import com.holley.mvc.common.util.CacheCloudUtil;

public class RedisShiroSessionDAO extends AbstractSessionDAO {

    public final static Logger logger               = Logger.getLogger(RedisShiroSessionDAO.class);
    // private String keyPrefix = "shiro_redis_session_";
    private Long               globalSessionTimeout = 1800000L;

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);

    }

    @Override
    public void delete(Session session) {
        deleteSession(session);
    }

    @Override
    public Collection<Session> getActiveSessions() {
        return null;
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            logger.error("session id is null");
            return null;
        }
        Session session = null;
        try {
            session = (Session) SerializationUtils.deserialize(CacheCloudUtil.getByte(getByteKey(sessionId)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return session;
    }

    private void saveSession(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            logger.error("session or session id is null");
            return;
        }
        byte[] key = getByteKey(session.getId());
        byte[] value = sessionToByte(session);
        // session.setTimeout(sessionTimeOut);
        CacheCloudUtil.setByteEx(key, value, globalSessionTimeout.intValue());
    }

    private void deleteSession(Session session) {
        if (session == null || session.getId() == null) {
            logger.error("session or session id is null");
            return;
        }
        byte[] key = getByteKey(session.getId());
        CacheCloudUtil.delKey(key);
    }

    /**
     * 获得byte[]型的key
     * 
     * @param key
     * @return
     */
    private byte[] getByteKey(Serializable sessionId) {
        return sessionId.toString().getBytes();
    }

    // 把session对象转化为byte保存到redis中
    public byte[] sessionToByte(Session session) {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        byte[] bytes = null;
        try {
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(session);
            bytes = bo.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public void setGlobalSessionTimeout(Long globalSessionTimeout) {
        this.globalSessionTimeout = globalSessionTimeout;
    }

}
