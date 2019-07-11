package com.hcq.fts.config;

import java.util.ResourceBundle;

/**
 * @Author: solor
 * @Since: 2.0
 * @Description:
 */
public enum Config {

    SINGLETON_INSTANCE(ResourceBundle.getBundle("jfts-client"));

    private ResourceBundle bundle;
    public static final String LOGIN="LOGIN";
    public static final String LOGOUT="LOGOUT";
    public static final String JFTS_LOCAL="jfts.localHost";
    public static final String JFTS_SERVER="jfts.remoteHost";
    public static final String JFTS_PORT="jfts.port";
    public static final String JFTS_ENCODING="jfts.encoding";
    public String getLogin() {return Config.LOGIN;}
    public String getLogout() {return Config.LOGOUT;}
    public String getLocalHost(){
        return this.getKey(Config.JFTS_LOCAL);
    }
    public String getServerHost(){
        return this.getKey(Config.JFTS_SERVER);
    }
    public int getServerPort(){
        return Integer.valueOf(this.getKey(Config.JFTS_PORT));
    }
    public String getEncoding(){
        return this.getKey(Config.JFTS_ENCODING);
    }
    public String getKey(String key){
        return bundle.getString(key);
    }



    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    Config(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public static Config getInstance() {
        return SINGLETON_INSTANCE;
    }

}
