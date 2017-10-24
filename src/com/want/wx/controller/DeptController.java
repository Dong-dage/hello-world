package com.want.wx.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.want.wx.pojo.Dept;
import com.want.wx.pojo.Emp;
import com.want.wx.service.DeptService;
import com.want.wx.service.EmpService;
import com.want.wx.util.LdapService;
import com.want.wx.util.PageResult;
import com.want.wx.util.SetList;

import net.sf.json.JSONSerializer;

@Controller
public class DeptController {
	
	public final ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private DeptService	deptService;
	@Autowired
	private EmpService	empService;	
	@Autowired
	private LdapService ldapService;
	private Logger logger = Logger.getLogger(DeptController.class);
	
	//通过netweaver接口跳转到此登录接口
    @RequestMapping("/login")   
    public ModelAndView emplog2(HttpServletRequest req){  
    	//String loginId=req.getParameter("loginId");   
    	String loginId=req.getParameter("key");
    	String domain=req.getParameter("domain");
    	logger.info("addressBookExport---登录者账号---loginId: "+loginId+"  ++++爱旺旺环境域名---domain: "+domain);
    	req.setAttribute("loginId", loginId); 
        return new ModelAndView("/org");
    }
    
	//通过用户名密码进行登录 
    @RequestMapping("/loginJudge")  
    public void loginJudge(HttpServletRequest req,HttpServletResponse response) {
      	response.setContentType("application/json");
      	PrintWriter out=null;
      	try {
      		out=response.getWriter();
      		
      		String loginId=req.getParameter("loginId");
        	String password=req.getParameter("password");
        	req.setAttribute("loginId", loginId); 
        	boolean isValid = true;
        	try {
    			isValid = ldapService.checkADLDSLdapUserInfo(loginId, password);
    			System.out.println(isValid);
    		} catch (NamingException e) {
    			logger.error("工號:"+loginId+",--------------e:"+e);
    			isValid = false;
    		}
        	
        	String json="{\"result\":\""+isValid+"\"}";
        	out.write(json);
        	   
  		} catch (Exception e) {
  			e.printStackTrace();
  			out.flush();
  			out.close();
  		}
  	      
    }
       
    //用户名的判断                                                                       
    @RequestMapping("/uIdJudge")  
    public void empNoJudge(HttpServletRequest req,HttpServletResponse response) {  
      	response.setContentType("application/json");
      	PrintWriter out=null;
      	try {
      		out=response.getWriter();
      		
      		Emp emp=new Emp();
    		emp.setuId(req.getParameter("uId"));
    		
      	   	boolean falg=empService.uIdJudge(emp);
        	   	
        	String json="{\"result\":\""+falg+"\"}";
        	out.write(json);
        	   
  		} catch (Exception e) {
  			e.printStackTrace();
  			out.flush();
  			out.close();
  		}
  	      
    }

	//查询所有部门组织菜单
	@RequestMapping(value="/queryDeptList")
	public void queryDeptList(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("application/json");
		PrintWriter out = null;		
		List<Dept> deptList = new ArrayList<Dept>();	
		try{
			out=response.getWriter();			
			deptList=deptService.queryDeptList();
			String  json=mapper.writeValueAsString(deptList);
			out.write(json);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
		}		
	}
	
	//通过登录用户查询部门组织菜单
	@RequestMapping(value="/queryDeptListByLoginId")
	public void queryDeptListByLoginId(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("application/json");
		PrintWriter out = null;		
		List<Dept> deptList = new ArrayList<Dept>();	
		try{
			out=response.getWriter();		
			String loginId=request.getParameter("loginId");
			List agentNameList=deptService.queryAgentName(loginId);
			List agentIdList=deptService.queryAgentId(loginId);
			
			if (agentNameList.size()>0 && agentNameList.contains("admin")) {
				deptList=deptService.queryDeptList();
			}else if (agentNameList.size()>0 && !agentNameList.contains("admin")) {
//				deptList=deptService.queryDeptList(agentIdList);
				deptList=deptService.queryDeptListByLoginId(loginId);
			} 
			
			List<Dept> deptList2=new SetList<Dept>();		
			List<String> dept1=new ArrayList<String>();
			
			if (deptList.size()>0) {
				for (Dept dept : deptList) {	
					dept1.add(dept.getId());
				}
				//根据部门id进行去重
				for (String deptStr : dept1) {
					deptList.stream()
						.filter(o -> deptStr.equals(o.getId()))
						.findFirst()
						.ifPresent(p -> deptList2.add(p));				
				}
			}
		
			String  json=mapper.writeValueAsString(deptList2);
			out.write(json);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
		}		
	}
	//通过父部门id查询子部门
	@RequestMapping("/queryChildDeptList")
	public void queryChildDeptList(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("application/json");
		PrintWriter out = null;		
		List<Dept> childDeptList = new ArrayList<Dept>();	
		try{
			out=response.getWriter();	
			String parentId=request.getParameter("parentId");	
			childDeptList=deptService.queryChildDeptList(parentId);
			String  json=mapper.writeValueAsString(childDeptList);
			out.write(json);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
		}
	}
	
	//通过a标签跳转到org.jsp页面
	@RequestMapping("/toOrg")
	public ModelAndView toOrg (HttpServletRequest request,HttpServletResponse response){
		String loginId=request.getParameter("loginId");	
  	   	request.setAttribute("loginId", loginId);
	    ModelAndView mv = new ModelAndView();
	    mv.addObject("loginId",loginId);
	    mv.setViewName("/org");
	    return mv;
	}	
	
	//查询部门中的员工
	@RequestMapping("/queryEmpList")
	public void queryEmpList(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("application/json");
		PrintWriter out = null;		
		List<Emp> empList = new ArrayList<Emp>();	
		try{
			out=response.getWriter();	
			String dId=request.getParameter("dId");	
			empList=empService.queryEmpList(dId);
			String  json=mapper.writeValueAsString(empList);
			out.write(json);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
		}
	}
	
	//通过部门id导出excel表格
	@RequestMapping("/exportFileBydId")
	public ModelAndView exportFileBydId(HttpServletRequest request,HttpServletResponse response) {
		String status=request.getParameter("status");	
		String[] dIds=request.getParameterValues("dId");
		empService.exportFileBydId(response, dIds,status);
	    ModelAndView mv = new ModelAndView();
	    mv.addObject("type", "export");
	    mv.setViewName("/success");
	    return mv;
	}
	
	//通过工号导出excel
//	@RequestMapping("/exportFileByuId")
//    public ModelAndView exportFileByuId(HttpServletRequest request,HttpServletResponse response) {
//		
////		String[] dIds=request.getParameterValues("dId");
////        service.exportFile(response, dIds);
//		String[] uIds=request.getParameterValues("uId"); 
//        service.exportFileByUserid(response, uIds);
//        ModelAndView mv = new ModelAndView();
//        mv.addObject("type", "export");
//        mv.setViewName("/success");
//        return mv;
//    }
	
	//分页查询所有员工 
	@RequestMapping("/queryEmpPage") 
	public void queryEmpPage(HttpServletRequest request,HttpServletResponse response) {
		response.setContentType("application/json");
		PrintWriter out = null;
		List<Map<String, Object>> empList =null;
		PageResult page=null; 
		Map<String, Object> map = new HashMap<String, Object>();
		try {	
			out = response.getWriter();
			String currentPage  = request.getParameter("currentPage");
			String numPerPage  = request.getParameter("numPerPage");
			String status = request.getParameter("status");
			String dId = request.getParameter("dId");	
			String[] dIds=dId.split(",");
			
			List list=new ArrayList<>();
			List list1=new ArrayList<>();
			for (int i = 0; i < dIds.length; i++) {
				if("".equals(currentPage)||"".equals(numPerPage)){ 
					page =empService.queryPage(1, 100,dIds[i],status);  				
				}else{ 
					page =empService.queryPage(Integer.valueOf(currentPage),Integer.valueOf(numPerPage),dIds[i],status);  
				} 
				list1=page.getResultList();
				System.out.println(list1.size());
				list.addAll(list1);
				System.out.println(list.size());
			}
			
			empList=new ArrayList<Map<String,Object>>(); 
			for (int i = 0,len=list.size();i<len; i++) {
				Map<String, Object> maps=new HashMap<String, Object>();
				Map mapRe=(Map)list.get(i);		
				maps.put("uName", mapRe.get("USER_NAME"));
				maps.put("uId", mapRe.get("USERID"));
				maps.put("position", mapRe.get("POSITION") == null ? "" : mapRe.get("POSITION"));
				maps.put("mobile", mapRe.get("MOBILE"));
				maps.put("status", mapRe.get("STATUS"));
				empList.add(maps); 
			}
		} catch (Exception e1) { 
			e1.printStackTrace();				
		}finally{ 				
			map.put("totalPage", page.getTotalPages());  
			map.put("currentPage", page.getCurrentPage());  
			map.put("totalRows", page.getTotalRows());  
			map.put("numPerPage", page.getNumPerPage()); 
			map.put("empList", empList);  
			//必须设置字符编码，否则返回json会乱码 
			response.setContentType("text/html;charset=UTF-8");   
			out.write(JSONSerializer.toJSON(map).toString());
			out.flush();
			out.close();
		}		 			
	} 
	
	
	
}
