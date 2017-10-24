package com.want.wx.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;


@Service
public class LdapService {
	private static ResourceBundle bundle = ResourceBundle.getBundle("/connection");
	private static String url = bundle.getString("ldapUrl");
	private static String principal = bundle.getString("ldapUser");
	private static String password = bundle.getString("ldapPwd");
	private static String baseDir = bundle.getString("ldapBaseDir");
	
	public boolean  checkADLDSLdapUserInfo(String userId,String password) throws NamingException {
    	boolean falg=false;
    	Hashtable<String,String> hash = new Hashtable<String,String>();
    	 hash.put("java.naming.factory.initial","com.sun.jndi.ldap.LdapCtxFactory");
    	 hash.put(Context.PROVIDER_URL, url);
    	 hash.put(Context.SECURITY_AUTHENTICATION, "simple");
    	 hash.put(Context.REFERRAL, "follow");
    	 hash.put(Context.SECURITY_PRINCIPAL, userId);		
    	 hash.put(Context.SECURITY_CREDENTIALS, password);
		
		 
    	SearchControls ctls = new SearchControls();		
    	ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
    	LdapContext ldapContext=null;
    	try {
    		ldapContext = new InitialLdapContext(hash, null);
    		falg=true;
    	} catch (NamingException e) {
    		throw e;
    	}finally{
    		colseLdapConn(ldapContext);
    	}		
    	return falg;
    }
	
	private void colseLdapConn(LdapContext ldapContext) {
		try {
			if(null!=ldapContext){
				ldapContext.close();					
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	
	public String getUserName(String userId) throws NamingException{
		InitialDirContext ctx = null;
		try{
			Hashtable<String,String> hash = new Hashtable<String,String>();
		      hash.put("java.naming.factory.initial","com.sun.jndi.ldap.LdapCtxFactory");
		      hash.put(Context.PROVIDER_URL, "ldap://10.0.0.66:389/");
		      hash.put(Context.SECURITY_AUTHENTICATION, "simple");
		      hash.put(Context.SECURITY_PRINCIPAL, "portaladmin@want-want.com");
		      hash.put(Context.SECURITY_CREDENTIALS, "portaladmin");
		      hash.put(Context.REFERRAL, "follow");
		      ctx = new InitialLdapContext(hash, null);
		          
		      String[] userAttrs = {
		   		      "sn", "sAMAccountName","displayName", "givenName",
		   		      "mail", "distinguishedName",
		   		      "memberOf","department","title","telephoneNumber","company"};
		      
		      SearchControls ctls = new SearchControls();
		      ctls.setReturningAttributes(userAttrs);
		      ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		      String baseDir = "OU=旺旺集团,DC=want-want,DC=com";
		      SearchResult result = (SearchResult) ctx.search(baseDir,"sAMAccountName=" + userId, ctls).next();
		      Attributes attrs = result.getAttributes();
		      return attrs.get("displayName").get(0).toString();
		}
		catch (NamingException e) {
			e.printStackTrace();
		}
		finally{
			if(ctx!=null){
				ctx.close();
			}
		}
		return StringUtils.EMPTY;
	}
	
	public List<String> getEmpGroups(String userId) throws NamingException {
		InitialDirContext ctx  = null;
		try {
			Hashtable<String, String> hash = new Hashtable<String, String>();
			hash.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
			hash.put(Context.SECURITY_AUTHENTICATION, "simple");
			hash.put(Context.PROVIDER_URL, url);
			hash.put(Context.SECURITY_PRINCIPAL, principal);		
			hash.put(Context.SECURITY_CREDENTIALS, password);
			
			hash.put(Context.REFERRAL, "follow");
			ctx = new javax.naming.ldap.InitialLdapContext(hash, null);
			String[] userAttrs = { "memberOf" };
			SearchControls ctls = new SearchControls();
			ctls.setReturningAttributes(userAttrs);
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			SearchResult result = (SearchResult) ctx.search(baseDir, "cn=" + userId, ctls).next();
			Attribute attr = result.getAttributes().get("memberOf");
			List<String> groupList = new ArrayList<String>();
			for (int i = 0; i < attr.size(); i++) {
				groupList.add(attr.get(i).toString().split(",")[0].split("=")[1]);
			}
			return groupList;
		} 
		catch (Exception e) {
			throw e;
		}finally{
			if(ctx!=null){
				ctx.close();
			}
		}	
	}
	
	public static void main(String[] args){
		try {
			new LdapService().getEmpGroups("00004971").stream().forEach(System.out::println);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
