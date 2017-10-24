package com.want.wx.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.want.wx.pojo.Tag;
import com.want.wx.pojo.User;
import com.want.wx.pojo.UserBean;
import com.want.wx.service.DeptService;
import com.want.wx.service.TagService;
import com.want.wx.service.UserService;
import com.want.wx.util.CommonUtil;
import com.want.wx.util.PageResult;
import com.want.wx.util.WxUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@Controller
public class TagController {
	
	public final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private TagService	tagService;
	
	@Autowired
	private DeptService	deptService;
	
	@Autowired
	private UserService userservice;
	
	
	
	//查询标签
	@RequestMapping("/queryTagList")
	public void queryTag(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("application/json");
		PrintWriter out = null;		
		ArrayList<Tag> tagList = new ArrayList<Tag>();	
		try{
			out=response.getWriter();	
			/*String usid = request.getParameter("usid");
			tagList=(ArrayList<Tag>) tagService.queryTag(usid);*/
			tagList=(ArrayList<Tag>) tagService.queryTag();			
			String  json=objectMapper.writeValueAsString(tagList);
			out.write(json);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
		}		
	}
	
	//通过登录用户查询标签
	@RequestMapping("/queryTagByLoginId")
	public void queryTagByLoginId(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("application/json");
		PrintWriter out = null;		
		List<Tag> tagList = new ArrayList<Tag>();	
		try{
			out=response.getWriter();	
			String loginId = request.getParameter("loginId");
			
			List agentNameList=deptService.queryAgentName(loginId);
			List agentIdList=deptService.queryAgentId(loginId);
			
			if (agentNameList.size()>0 && agentNameList.contains("admin")) {
				tagList=tagService.queryTag();
			}else if (agentNameList.size()>0 && !agentNameList.contains("admin")) {
				tagList=tagService.queryTagList(agentIdList);
			}
			
			String  json=objectMapper.writeValueAsString(tagList);
			out.write(json);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
		}		
	}
			
	//通过a标签跳转到tag.jsp页面
	@RequestMapping("/toTag")
	public ModelAndView toTag (HttpServletRequest request,HttpServletResponse response){
		String loginId=request.getParameter("loginId");	
	  	request.setAttribute("loginId", loginId);
		ModelAndView mv = new ModelAndView();
		mv.addObject("loginId",loginId);
		mv.setViewName("/tag");
		return mv;
	}
	
		
	//获取标签下的用户
		@RequestMapping("/getTagUser")
		public void getTagUser(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
			response.setContentType("application/json");
			PrintWriter out = null;
			
			String access_token=CommonUtil.getToken("appID","appsecret");		
			String status=request.getParameter("status");
			String tId = request.getParameter("tId");
			String[] tIds=tId.split(",");			
			WxUtil wxUtil = new WxUtil();
			String result = null;
			List<User> list=new ArrayList<>();
			for (int i = 0; i < tIds.length; i++) {
				String wechatUserList = wxUtil.getWxList(access_token, tIds[i]);
				JSONObject wechatUserListJson = JSONObject.fromObject(wechatUserList);
				String code = wechatUserListJson.getString("errcode");				
				if ("0".equals(code)) {	
					JSONArray wechatUserListJsonArray = wechatUserListJson.getJSONArray("userlist");
					if (wechatUserListJsonArray.size()>0) {
						@SuppressWarnings("unchecked")
						ArrayList<UserBean> arrList = (ArrayList<UserBean>) JSONArray.toCollection(wechatUserListJsonArray, UserBean.class);	
						userservice.deleteTempUserId();
						for (UserBean userBean : arrList) {
							String userId = userBean.getUserid();		
							//System.out.println(userId);
							userservice.saveTempUserId(userId);
							//userIdList.add(userId);							
						}										
						List<User> usersList=userservice.queryUserById(status);
						if (usersList.size()>0) {
							list.addAll(usersList);
						}						
					}					
				}				
			}
			result = objectMapper.writeValueAsString(list);
			
			try {
				out = response.getWriter();
				out.println(result);
			} catch (IOException E) {
				E.printStackTrace();
			} finally {
				out.flush();
				out.close();
			}
		}
		
		// 分页查询所有员工
		@RequestMapping("/queryTagUserPage")
		public void queryPage(HttpServletRequest request, HttpServletResponse response) {
			response.setContentType("application/json");
			PrintWriter out = null;
			List<Map<String, Object>> empList = null;
			PageResult page = null;
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				out = response.getWriter();
				String currentPage = request.getParameter("currentPageTag");
				String numPerPage = request.getParameter("numPerPageTag");
				
				String access_token=CommonUtil.getToken("appID","appsecret");
				String wechat_tagid = request.getParameter("wechat_tagid");
				String status=request.getParameter("status");
				WxUtil wxUtil = new WxUtil();
				String wechatUserList = wxUtil.getWxList(access_token, wechat_tagid);

				JSONObject wechatUserListJson = JSONObject.fromObject(wechatUserList);

				String code = wechatUserListJson.getString("errcode");
				String result = null;
				if ("0".equals(code)) {
					JSONArray wechatUserListJsonArray = wechatUserListJson.getJSONArray("userlist");
					@SuppressWarnings("unchecked")
					ArrayList<UserBean> arrList = (ArrayList<UserBean>) JSONArray.toCollection(wechatUserListJsonArray, UserBean.class);	
					userservice.deleteTempUserId();
					for (UserBean userBean : arrList) {
						String userId = userBean.getUserid();		
						//System.out.println(userId);
						userservice.saveTempUserId(userId);
						//userIdList.add(userId);							
					}										
					if ("".equals(currentPage) || "".equals(numPerPage)) {
						page = userservice.queryPage(1, 100,status);
					} else {
						page = userservice.queryPage(Integer.valueOf(currentPage), Integer.valueOf(numPerPage), status);
					}
					List list = page.getResultList();
					empList = new ArrayList<Map<String, Object>>();
					for (int i = 0, len = list.size(); i < len; i++) {
						Map<String, Object> maps = new HashMap<String, Object>();
						Map mapRe = (Map) list.get(i);
						maps.put("uName", mapRe.get("USER_NAME"));
						maps.put("uId", mapRe.get("USERID"));
						maps.put("position", mapRe.get("POSITION"));
						maps.put("mobile", mapRe.get("MOBILE"));
						maps.put("status", mapRe.get("STATUS"));
						empList.add(maps);
					}
				}			
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				map.put("totalPage", page.getTotalPages());
				map.put("currentPage", page.getCurrentPage());
				map.put("totalRows", page.getTotalRows());
				map.put("numPerPage", page.getNumPerPage());
				map.put("empList", empList);
				// 必须设置字符编码，否则返回json会乱码
				response.setContentType("text/html;charset=UTF-8");
				out.write(JSONSerializer.toJSON(map).toString());
				out.flush();
				out.close();
			}
		}

		// 通过userid导出Excel
		@RequestMapping("exportByUserid")
		public ModelAndView exportFile(HttpServletRequest request, HttpServletResponse response) {
			String status = request.getParameter("status");
			String[] tIds = request.getParameterValues("tId");

			List<String> userIdList = new ArrayList<String>();

			for (String strtId : tIds) {
				if (strtId.contains(",") == true) {
					String[] tIds2 = strtId.split(",");
					for (String tId : tIds2) {
						String access_token = CommonUtil.getToken("appID", "appsecret");
						WxUtil wxUtil = new WxUtil();
						String wechatUserList = wxUtil.getWxList(access_token, tId);
						JSONObject wechatUserListJson = JSONObject.fromObject(wechatUserList);
						String code = wechatUserListJson.getString("errcode");

						if ("0".equals(code)) {
							JSONArray wechatUserListJsonArray = wechatUserListJson.getJSONArray("userlist");
							@SuppressWarnings("unchecked")
							ArrayList<UserBean> arrList = (ArrayList<UserBean>) JSONArray
									.toCollection(wechatUserListJsonArray, UserBean.class);
							for (UserBean users : arrList) {
								String userId = users.getUserid();
								userIdList.add(userId);
							}
						}
					}
				} else {
					String access_token = CommonUtil.getToken("appID", "appsecret");
					WxUtil wxUtil = new WxUtil();
					String wechatUserList = wxUtil.getWxList(access_token, strtId);
					JSONObject wechatUserListJson = JSONObject.fromObject(wechatUserList);
					String code = wechatUserListJson.getString("errcode");

					if ("0".equals(code)) {
						JSONArray wechatUserListJsonArray = wechatUserListJson.getJSONArray("userlist");
						@SuppressWarnings("unchecked")
						ArrayList<UserBean> arrList = (ArrayList<UserBean>) JSONArray.toCollection(wechatUserListJsonArray,
								UserBean.class);
						for (UserBean users : arrList) {
							String userId = users.getUserid();
							userIdList.add(userId);
						}
					}
				}
			}

			// String[] userIds = userIdList.toArray(new String[userIdList.size()]);
			// userservice.exportByUserId(response,userIds, status);

			userservice.deleteTempUserId();
			for (String us : userIdList) {
				//System.out.println(us);
				userservice.saveTempUserId(us);
			}
			userservice.exportByPermUserId(response, status);

			ModelAndView mv = new ModelAndView();
			mv.addObject("type", "export");
			mv.setViewName("/success");
			return mv;
		}
	
	
}
