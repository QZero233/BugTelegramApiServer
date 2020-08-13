package com.qzero.bt.authorize.permission;

import com.qzero.bt.authorize.data.TokenEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Normally use @PermissionConfig with minPermissionLevel and deniedPermissionLevels and return void
 * If the method return boolean and take Token as an only parameter,it'll use the method's return value to check
 */
@Component("permissionConfigurator")
public class PermissionConfigurator {

    private Map<String, PermissionChecker> permissionConfigMap = new HashMap<>();

    private interface PermissionChecker {
        boolean check(TokenEntity tokenEntity) throws Exception;
    }

    public PermissionConfigurator() throws Exception {
        loadConfigMap();
    }

    private void loadConfigMap() throws Exception {
        Method[] methods = getClass().getDeclaredMethods();
        for (Method method : methods) {
            PermissionConfig config = method.getDeclaredAnnotation(PermissionConfig.class);
            if (config == null)
                continue;

            String permissionName = config.value();
            if ((method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.class))
                    && method.getParameterCount()==1
                    && method.getParameterTypes()[0].equals(TokenEntity.class)) {
                //Use method

                PermissionChecker checker=tokenEntity -> {
                    if (tokenEntity == null || tokenEntity.getPermissionLevel() < TokenEntity.PERMISSION_LEVEL_APPLICATION)
                        throw new IllegalArgumentException("Wrong token");

                    return (boolean) method.invoke(PermissionConfigurator.this,tokenEntity);
                };

                permissionConfigMap.put(permissionName,checker);

            } else {
                //Use annotation
                int minPermissionLevel = config.minPermissionLevel();
                int[] deniedPermissionLevel = config.deniedPermissionLevels();
                PermissionChecker checker = tokenEntity -> checkByPermissionLevel(tokenEntity, minPermissionLevel, deniedPermissionLevel);
                permissionConfigMap.put(permissionName, checker);
            }

        }
    }

    private boolean checkByPermissionLevel(TokenEntity tokenEntity, int minPermissionLevel, int[] deniedPermissionLevels) {
        if (tokenEntity == null || tokenEntity.getPermissionLevel() < TokenEntity.PERMISSION_LEVEL_APPLICATION)
            throw new IllegalArgumentException("Wrong token");

        int level = tokenEntity.getPermissionLevel();
        if (level < minPermissionLevel)
            return false;

        if (deniedPermissionLevels != null) {
            for (int denied : deniedPermissionLevels) {
                if (level == denied)
                    return false;
            }
        }

        return true;
    }

    public boolean checkPermission(TokenEntity tokenEntity,String permissionName) throws Exception {
        if(tokenEntity==null || permissionName==null)
            throw new NullPointerException("Token and permissionName can not be null");
        if(!permissionConfigMap.containsKey(permissionName))
            throw new IllegalArgumentException(String.format("Permission %s dost not exist", permissionName));

        PermissionChecker checker=permissionConfigMap.get(permissionName);
        return checker.check(tokenEntity);
    }


    @PermissionConfig(value = PermissionNameList.PERMISSION_MODIFY_ACCOUNT,minPermissionLevel = TokenEntity.PERMISSION_LEVEL_GLOBAL)
    private void modifyAccount(){}

    @PermissionConfig(value = PermissionNameList.PERMISSION_MODIFY_TOKEN)
    private void modifyToken(){}

    @PermissionConfig(value = PermissionNameList.PERMISSION_READ_USER_INFO,minPermissionLevel = TokenEntity.PERMISSION_LEVEL_ANYONE)
    private void readUserInfo(){}

    @PermissionConfig(value = PermissionNameList.PERMISSION_UPDATE_USER_INFO,minPermissionLevel = TokenEntity.PERMISSION_LEVEL_APPLICATION)
    private void updateUserInfo(){}

}
