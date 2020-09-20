package com.qzero.bt.common.permission;

import com.qzero.bt.common.exception.ErrorCodeList;
import com.qzero.bt.common.exception.PermissionDeniedException;
import com.qzero.bt.common.exception.ResourceDoesNotExistException;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.authorize.dao.TokenDao;
import com.qzero.bt.common.authorize.data.TokenEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Transactional
@Component
public class PermissionCheckInterceptor implements HandlerInterceptor {

    private Logger log= LoggerFactory.getLogger(getClass());

    @Autowired
    private TokenDao tokenDao;

    @Autowired
    @Qualifier("permissionConfigurator")
    private PermissionConfigurator permissionConfigurator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod method= (HandlerMethod) handler;

        DisablePermissionCheck disablePermissionCheck=method.getMethodAnnotation(DisablePermissionCheck.class);
        if(disablePermissionCheck!=null)
            return true;

        PermissionCheck checkAnnotation=method.getMethodAnnotation(PermissionCheck.class);
        if(checkAnnotation==null){
            log.trace(String.format("Start permission check\nMethod %s\nJust check token",method.getMethod().getName()));
        }else{
            log.trace(String.format("Start permission check\nMethod %s\nPermissionName %s",method.getMethod().getName(),checkAnnotation.value()));
        }

        String tokenId=request.getHeader("token_id");
        String ownerUserName=request.getHeader("owner_user_name");

        if(tokenId==null || ownerUserName==null)
            throw new ResponsiveException(ErrorCodeList.CODE_BAD_REQUEST_PARAMETER,"TokenId and OwnerUserName can not be empty");

        TokenEntity tokenEntity=tokenDao.getTokenById(tokenId);

        if(tokenEntity==null)
            throw new ResourceDoesNotExistException(ErrorCodeList.CODE_ILLEGAL_TOKEN,"Token does not exist");

        if(tokenEntity.getOwnerUserName().equals(ownerUserName))
            throw new PermissionDeniedException(ErrorCodeList.CODE_ILLEGAL_TOKEN,"Token owner name does not match");

        if(tokenEntity.getEndTime()>0 && tokenEntity.getEndTime()<System.currentTimeMillis()){
            tokenDao.deleteToken(tokenEntity);
            throw new PermissionDeniedException(ErrorCodeList.CODE_ILLEGAL_TOKEN,"Token expired");
        }

        if(checkAnnotation==null){
            return true;
        }

        if(checkAnnotation.permissions()==null){
            //Check single permission
            if(!permissionConfigurator.checkPermission(tokenEntity,checkAnnotation.value()))
                throw new PermissionDeniedException(ErrorCodeList.CODE_PERMISSION_DENIED,"Permission denied");
        }else{
            //Check multi permissions
            String[] permissions=checkAnnotation.permissions();
            for(String permission:permissions){
                if(!permissionConfigurator.checkPermission(tokenEntity,permission))
                    throw new PermissionDeniedException(ErrorCodeList.CODE_PERMISSION_DENIED,"Permission denied");
            }
        }

        return true;
    }
}
