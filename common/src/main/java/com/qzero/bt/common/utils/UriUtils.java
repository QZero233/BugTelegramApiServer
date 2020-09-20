package com.qzero.bt.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

public class UriUtils {

    private static Logger log= LoggerFactory.getLogger(UriUtils.class);

    public static String generateUri(String dataType,String dataId,String detail){
        try {
            URI uri=new URI(dataType,dataId,null,null,detail);
            return uri.toString();
        } catch (URISyntaxException e) {
            log.error("Generate uri failed",e);
            return null;
        }
    }

}
