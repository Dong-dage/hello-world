package com.want.wx.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.want.wx.dao.DeptDao;
import com.want.wx.pojo.Dept;

@Repository
public class DeptDaoImpl implements DeptDao{
	
	@Autowired
	@Qualifier("wechatJdbcTemplate")
	public JdbcTemplate wechatJdbcTemplate;
	
	//查询第二层组织
	@Override
	public List<Dept> querySecondFloor(String pId) {
		// TODO Auto-generated method stub
		List<Dept> deptList=new ArrayList<Dept>(); 
		String sql="select DEPT_NAME from WECHAT.WECHAT_CONTACTS_DEPARTMENT where parent_id='"+pId+"'";
		deptList = wechatJdbcTemplate.query(sql, new RowMapper<Dept>(){			 
	        @Override
	        public Dept mapRow(ResultSet rs, int index) throws SQLException {
	        	Dept dept = new Dept();	        	
	        	dept.setName(rs.getString("DEPT_NAME"));
	        	//dept.setPId(rs.getString("pId"));
	            return dept;
	        };
		});
		return deptList;
	}
	//查询所有部门组织菜单
	@Override
	public List<Dept> queryDeptList() {
		// TODO Auto-generated method stub
		List<Dept> deptList=new ArrayList<Dept>(); 
		String sql="select DEPARTMENT_ID,DEPT_NAME,PARENT_ID from WECHAT.WECHAT_CONTACTS_DEPARTMENT order by department_id";
		deptList = wechatJdbcTemplate.query(sql , new RowMapper<Dept>(){			 
	        @Override
	        public Dept mapRow(ResultSet rs, int index) throws SQLException {            
	        	Dept dept = new Dept();	      
	        	dept.setId(rs.getString("DEPARTMENT_ID"));
	        	dept.setName(rs.getString("DEPT_NAME"));
	        	dept.setpId(rs.getString("PARENT_ID"));
	            return dept;
	            
	        };
		});
		return deptList;
	}
	
	//通过父部门id查询子部门
	@Override
	public List<Dept> queryChildDeptList(String parentId) {
		// TODO Auto-generated method stub
		List<Dept> deptList=new ArrayList<Dept>(); 
		String sql="select DEPARTMENT_ID,DEPT_NAME,PARENT_ID from WECHAT.WECHAT_CONTACTS_DEPARTMENT WHERE PARENT_ID='"+parentId+"'";
		deptList = wechatJdbcTemplate.query(sql , new RowMapper<Dept>(){			 
	        @Override
	        public Dept mapRow(ResultSet rs, int index) throws SQLException {            
	        	Dept dept = new Dept();	      
	        	dept.setId(rs.getString("DEPARTMENT_ID"));
	        	dept.setName(rs.getString("DEPT_NAME"));
	        	dept.setpId(rs.getString("PARENT_ID"));
	        	dept.setIsParent("true");
	            return dept;
	            
	        };
		});
		return deptList;
	}
	
	//通过登录用户查询部门组织菜单
	@Override
	public List<Dept> queryDeptListByLoginId(String loginId) {
		// TODO Auto-generated method stub
		String sql="select distinct DEPT_ID,DEPT_NAME from wechat.wechat_agent_dept "
				+ "where agent_id in (select distinct agent_id from wechat.wechat_agent_user where user_id='"+loginId+"')";
		
		List<Dept> deptList=new ArrayList<Dept>(); 
		deptList = wechatJdbcTemplate.query(sql.toString(), new RowMapper<Dept>(){			 
	        @Override
	        public Dept mapRow(ResultSet rs, int index) throws SQLException {            
	        	Dept dept = new Dept();	      
	        	dept.setId(rs.getString("DEPT_ID"));
	        	dept.setName(rs.getString("DEPT_NAME"));
	            return dept;            
	        };
		});
		return deptList;
	}
	
	//判断登录用户角色
	@Override
	public List queryAgentName(String loginId) {
		// TODO Auto-generated method stub
		String sql="select agent_name from wechat.wechat_agent_app where agent_id "
				+ " in (select agent_id from wechat.wechat_agent_user where user_id='"+loginId+"') ";
		return wechatJdbcTemplate.queryForList(sql, String.class);
	}
	
	@Override
	public List queryAgentId(String loginId) {
		// TODO Auto-generated method stub
		String sql="select agent_id from wechat.wechat_agent_user where user_id='"+loginId+"' ";
		return wechatJdbcTemplate.queryForList(sql, String.class);
	}
	
	//根据deptAgentIdList查询 DeptList
	@Override
	public List<Dept> queryDeptList(List agentIdList) {
		// TODO Auto-generated method stub
		List<Dept> deptList=new ArrayList<Dept>();
		List<Dept> deptList2=new ArrayList<Dept>();
		
		if (agentIdList.get(0)!=null) {
						
			for (Object  agentId : agentIdList) {
				String sql2="select distinct dept_id from wechat.wechat_agent_dept where agent_id='"+agentId+"' ";
				List<String> deptIds=wechatJdbcTemplate.queryForList(sql2, String.class);
				if (deptIds.size()>0) {
					StringBuffer sql=new StringBuffer();
					//sql.append("select distinct dept_id,dept_name,parent_dept_id from wechat.wechat_dept start with dept_id in( " );
					sql.append("select distinct DEPARTMENT_ID,DEPT_NAME,PARENT_ID from WECHAT.WECHAT_CONTACTS_DEPARTMENT  start with department_id in( " );
					int count = 0;
					for (String dId : deptIds) {
						count++;			
						if (count!=deptIds.size()) {
							sql.append(" '"+dId+"' ,");
						}else{
							sql.append("'"+dId+"'");
						}
					}					
					//sql.append(" ) connect by prior dept_id=parent_dept_id ");
					sql.append(" ) connect by prior DEPARTMENT_ID=PARENT_ID ");
					
					deptList = wechatJdbcTemplate.query(sql.toString(), new RowMapper<Dept>(){			 
				        @Override
				        public Dept mapRow(ResultSet rs, int index) throws SQLException {            
				        	Dept dept = new Dept();	      
				        	/*dept.setId(rs.getString("dept_id"));
				        	dept.setName(rs.getString("dept_name"));
				        	dept.setpId(rs.getString("parent_dept_id"));*/
				        	
				        	dept.setId(rs.getString("DEPARTMENT_ID"));
				        	dept.setName(rs.getString("DEPT_NAME"));
				        	dept.setpId(rs.getString("PARENT_ID"));
				            return dept;
				            
				        };
					});	
					
				}				
				deptList2.addAll(deptList);								
			}														
		}
						
		return deptList2;
	}
	
	/*@Override
	public List queryDeptAgentIdList(String loginId) {
		// TODO Auto-generated method stub
		List<String> deptIdList=new ArrayList<String>();
		List<Dept> deptList=new ArrayList<Dept>();
		StringBuffer sql3=new StringBuffer();
		String sql="select dept_agent_id from wechat.wechat_user where user_id='"+loginId+"' ";
		List deptAgentIdList = wechatJdbcTemplate.queryForList(sql, String.class);
		if (deptAgentIdList.size()>0) {
			sql3.append("select dept_id,dept_name,parent_dept_id from wechat.wechat_dept start with dept_id in( " );
			int count = 0;
			for (Object  deptAgentId : deptAgentIdList) {
				String sql2="select dept_id from wechat.wechat_dept_agent where dept_agent_id='"+deptAgentId+"' ";
				String deptId=wechatJdbcTemplate.queryForObject(sql2, String.class);
				
				count++;			
				if (count!=deptAgentIdList.size()) {
					sql3.append(" '"+deptId+"' ,");
				}else{
					sql3.append("'"+deptId+"'");
				}
			}
					
			sql3.append(" ) connect by prior dept_id=parent_dept_id ");
			
			deptList = wechatJdbcTemplate.query(sql3.toString(), new RowMapper<Dept>(){			 
		        @Override
		        public Dept mapRow(ResultSet rs, int index) throws SQLException {            
		        	Dept dept = new Dept();	      
		        	dept.setId(rs.getString("dept_id"));
		        	dept.setName(rs.getString("dept_name"));
		        	dept.setpId(rs.getString("parent_dept_id"));
		            return dept;
		            
		        };
			});							
		}
				
		return deptList;
	}*/
	
	
}
