package com.qzero.bt.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

public class UriUtils {

    private static Logger log= LoggerFactory.getLogger(UriUtils.class);

    public static String generateUri(String schema,String user,String host,String query,String fragment){
        try {
            URI uri=new URI(schema,user,host,0,null,query,fragment);
            return uri.toString();
        } catch (URISyntaxException e) {
            log.error("Generate uri failed",e);
            return null;
        }
    }

}
