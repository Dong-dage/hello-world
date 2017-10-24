package com.want.wx.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.want.wx.dao.EmpDao;
import com.want.wx.pojo.Emp;
import com.want.wx.util.PageResult;


@Repository
public class EmpDaoImpl implements EmpDao{
	
	@Autowired
	@Qualifier("wechatJdbcTemplate")
	private JdbcTemplate wechatJdbcTemplate;
	@Autowired
	@Qualifier("salesJdbcTemplate")
	private JdbcTemplate salesJdbcTemplate;
	
	//查询部门中的员工
	@Override
	public List<Emp> queryEmpList(String dId) {
		// TODO Auto-generated method stub
		List<Emp> empList=new ArrayList<Emp>(); 
//		String sql="select "	
//				+ "USER_NAME,USERID,POSITION,MOBILE,"
//				+ "DEPARTMENT_ID,"
////				+ "(SELECT DEPT_NAME from WECHAT.WECHAT_CONTACTS_DEPARTMENT where DEPARTMENT_ID=WECHAT.WECHAT_ADDRESS_BOOK.DEPARTMENT_ID) DEPARTMENT_ID,"
//				+ "CASE "
//				+ "WHEN STATUS = '1' THEN '已关注' "
//				+ "WHEN STATUS = '4' THEN '未关注' "
//				+ "ELSE '已禁用' END STATUS "
//				+ "FROM WECHAT.WECHAT_ADDRESS_BOOK "
//				+ "where DEPARTMENT_ID='"+dId+"'";
		String sql="SELECT "
				+ "a.USER_NAME,a.USERID,a.POSITION,a.MOBILE,"
				+ "CASE "
				+ "WHEN STATUS = '1' THEN '已关注' "
				+ "WHEN STATUS = '4' THEN '未关注' "
				+ "ELSE '已禁用' END STATUS "
				+ "FROM WECHAT.WECHAT_ADDRESS_BOOK a "
				+ "where a.DEPARTMENT_ID in(select b.DEPARTMENT_ID from WECHAT.WECHAT_CONTACTS_DEPARTMENT b  START WITH b.DEPARTMENT_ID='"+dId+"' CONNECT BY NOCYCLE PRIOR b.DEPARTMENT_ID = b.PARENT_ID)";
		empList = wechatJdbcTemplate.query(sql , new RowMapper<Emp>(){
	        @Override
	        public Emp mapRow(ResultSet rs, int index) throws SQLException {
	        	Emp emp = new Emp();
	        	emp.setuName(rs.getString("USER_NAME"));
	        	emp.setuId(rs.getString("USERID"));
	        	emp.setPosition(rs.getString("POSITION"));
	        	emp.setMobile(rs.getString("MOBILE"));	        	        	
	        	emp.setStatus(rs.getString("STATUS"));	        	
	            return emp;
	        };
		});
		return empList;
	}
	
	//通过departmentid导出多个部门中的员工
	@Override
	public List<Emp> queryEmpListBydId(String[] dIds,String status) {
		// TODO Auto-generated method stub	
		
		String[] strs = dIds[0].split(",");
		
		List<String> list1=new ArrayList<String>();		
		list1=Arrays.asList(strs);
		//排除掉99882 经销商自有业代
		List<String> list11=list1.stream().filter(o -> !o.equals("99882")).distinct().collect(Collectors.toList());
			
		List<Emp> empList=new ArrayList<Emp>(); 
		if (list11.size()>0) {
			StringBuffer dIdsSB=new StringBuffer();		
			for(int i=0;i<list11.size();i++){
				if(i==list11.size()-1){
					dIdsSB.append("'"+list11.get(i)+"'");	 
				 }else{
					 dIdsSB.append("'"+list11.get(i)+"',");
				 }
			}
			
			String sql="SELECT distinct "
//					+ "c.COMPANY_NAME,c.BRANCH_NAME,"
					+ "cbv.COMPANY_NAME,cbv.BRANCH_NAME,"				
					+ "a.USER_NAME,a.USERID,a.POSITION,a.MOBILE,"
					+ "(SELECT listagg(DEPT_NAME,'－') within GROUP(ORDER BY level DESC) FROM WECHAT.WECHAT_CONTACTS_DEPARTMENT START WITH department_id = u.department_id CONNECT BY prior parent_id=department_id) DEPT_NAME,"
					+ "CASE WHEN a.STATUS = '1' THEN '已关注' WHEN a.STATUS = '4' THEN '未关注' ELSE '已禁用' END STATUS "
					+ "FROM WECHAT.WECHAT_ADDRESS_BOOK a "
//					+ "left join  SALES.EMP_POSITION_ORG_VIEW c on c.EMP_ID=a.USERID "
					+ "left join SALES.CUSTOMER cus on cus.ID=a.USERID  "
					+ "left join sales.company_branch_view cbv on cus.branch_id=cbv.branch_id "								 
					+ "left join (select w.USERID,w.department_id from wechat.wechat_userid_departmentid w,(select b.DEPARTMENT_ID from WECHAT.WECHAT_CONTACTS_DEPARTMENT b  START WITH b.DEPARTMENT_ID in("+dIdsSB.toString()+") CONNECT BY NOCYCLE PRIOR b.DEPARTMENT_ID = b.PARENT_ID) v where w.DEPARTMENT_ID=v.DEPARTMENT_ID)u on u.userid=a.userid "
					+ "where  exists(select 1 from wechat.wechat_userid_departmentid u ,(select bb.DEPARTMENT_ID from WECHAT.WECHAT_CONTACTS_DEPARTMENT bb  START WITH bb.DEPARTMENT_ID in("+dIdsSB.toString()+") CONNECT BY NOCYCLE PRIOR bb.DEPARTMENT_ID = bb.PARENT_ID) c where u.department_id=c.DEPARTMENT_ID and a.USERID=u.userid )"
					+ "and a.status in ("+status+")";
			//System.out.println(sql.toString());		
			
			empList = salesJdbcTemplate.query(sql.toString(), new RowMapper<Emp>(){
		        @Override
		        public Emp mapRow(ResultSet rs, int index) throws SQLException {       
		        	Emp emp = new Emp();
		        	emp.setCompanyName(rs.getString("COMPANY_NAME"));
		        	emp.setBranchName(rs.getString("BRANCH_NAME"));
		        	emp.setuName(rs.getString("USER_NAME"));
		        	emp.setuId(rs.getString("USERID"));
		        	emp.setPosition(rs.getString("POSITION"));
		        	emp.setMobile(rs.getString("MOBILE"));
		        	emp.setdId(rs.getString("DEPT_NAME"));	
		        	emp.setStatus(rs.getString("STATUS"));	
		        	return emp;
		        };
			});
		}
				
				
//		String sql="select b.DEPARTMENT_ID from WECHAT.WECHAT_CONTACTS_DEPARTMENT b  START WITH b.DEPARTMENT_ID in("+dIdsSB.toString()+") CONNECT BY NOCYCLE PRIOR b.DEPARTMENT_ID = b.PARENT_ID";
//		List<String> list=new ArrayList<String>();		
//		list=jdbcTemplate.queryForList(sql,java.lang.String.class);	
//				
//		StringBuffer sql2=new StringBuffer( );
//		sql2.append("SELECT  distinct c.COMPANY_NAME,c.BRANCH_NAME,a.USER_NAME,a.USERID,a.POSITION,a.MOBILE,"
//				+ "(SELECT listagg(DEPT_NAME,'－') within GROUP(ORDER BY level DESC) FROM WECHAT.WECHAT_CONTACTS_DEPARTMENT START WITH department_id = u.department_id CONNECT BY prior parent_id=department_id) DEPT_NAME,"
//				+ "CASE WHEN STATUS = '1' THEN '已关注' WHEN STATUS = '4' THEN '未关注' ELSE '已禁用' END STATUS "
//				+ "FROM WECHAT.WECHAT_ADDRESS_BOOK a "
//				+ "left join  SALES.EMP_POSITION_ORG_VIEW c on c.EMP_ID=a.USERID "
//				+ "left join wechat.wechat_userid_departmentid u on u.userid=a.userid "
//				+ "where a.USERID in (select u.userid from wechat.wechat_userid_departmentid u where u.department_id in( ");
//		int count = 0;
//		for (String dep : list) {	
//			count++;			
//			if (count!=list.size()) {
//				sql2.append("'"+dep+"'"+",");
//			}else{
//				sql2.append("'"+dep+"'");
//			}
//		}
//		sql2.append("))");
		
		
				
		List<String> list2=new ArrayList<String>();		
		list2=Arrays.asList(strs);	
		//过滤出99882 经销商自有业代
		List<String> list21=list2.stream().filter(o -> o.equals("99882")).distinct().collect(Collectors.toList());
		
		if (list21.size()>0) {
			String sql2="SELECT distinct "
					+ "c.COMPANY_NAME,c.BRANCH_NAME,"
//					+ "cbv.COMPANY_NAME,cbv.BRANCH_NAME,"				
					+ "a.USER_NAME,a.USERID,a.POSITION,a.MOBILE,"
					+ "(SELECT listagg(DEPT_NAME,'－') within GROUP(ORDER BY level DESC) FROM WECHAT.WECHAT_CONTACTS_DEPARTMENT START WITH department_id = u.department_id CONNECT BY prior parent_id=department_id) DEPT_NAME,"
					+ "CASE WHEN a.STATUS = '1' THEN '已关注' WHEN a.STATUS = '4' THEN '未关注' ELSE '已禁用' END STATUS "
					+ "FROM WECHAT.WECHAT_ADDRESS_BOOK a "
					+ "left join  SALES.EMP_POSITION_ORG_VIEW c on c.EMP_ID=a.USERID "
					+ "left join SALES.CUSTOMER cus on cus.ID=a.USERID  "
//					+ "left join sales.company_branch_view cbv on cus.branch_id=cbv.branch_id "								 
					+ "left join (select w.USERID,w.department_id from wechat.wechat_userid_departmentid w,(select b.DEPARTMENT_ID from WECHAT.WECHAT_CONTACTS_DEPARTMENT b  START WITH b.DEPARTMENT_ID in('"+list21.get(0)+"') CONNECT BY NOCYCLE PRIOR b.DEPARTMENT_ID = b.PARENT_ID) v where w.DEPARTMENT_ID=v.DEPARTMENT_ID)u on u.userid=a.userid "
					+ "where  exists(select 1 from wechat.wechat_userid_departmentid u ,(select bb.DEPARTMENT_ID from WECHAT.WECHAT_CONTACTS_DEPARTMENT bb  START WITH bb.DEPARTMENT_ID in('"+list21.get(0)+"') CONNECT BY NOCYCLE PRIOR bb.DEPARTMENT_ID = bb.PARENT_ID) c where u.department_id=c.DEPARTMENT_ID and a.USERID=u.userid )"
					+ "and a.status in ("+status+")";
			System.out.println(sql2.toString());		
			List<Emp> empList2=new ArrayList<Emp>(); 
			empList2 = salesJdbcTemplate.query(sql2.toString(), new RowMapper<Emp>(){
		        @Override
		        public Emp mapRow(ResultSet rs, int index) throws SQLException {       
		        	Emp emp = new Emp();
		        	emp.setCompanyName(rs.getString("COMPANY_NAME"));
		        	emp.setBranchName(rs.getString("BRANCH_NAME"));
		        	emp.setuName(rs.getString("USER_NAME"));
		        	emp.setuId(rs.getString("USERID"));
		        	emp.setPosition(rs.getString("POSITION"));
		        	emp.setMobile(rs.getString("MOBILE"));
		        	emp.setdId(rs.getString("DEPT_NAME"));	
		        	emp.setStatus(rs.getString("STATUS"));	
		        	return emp;
		        };
			});
			empList.addAll(empList2);
		}		
		return empList;
	}
	
	//通过departmentid分页查询部门中的员工
	@Override
	public PageResult queryPage(Integer currentPage, Integer numPerPage,String dId,String status) {
		// TODO Auto-generated method stub		
//		String sql="select b.DEPARTMENT_ID from WECHAT.WECHAT_CONTACTS_DEPARTMENT b  START WITH b.DEPARTMENT_ID='"+dId+"' CONNECT BY NOCYCLE PRIOR b.DEPARTMENT_ID = b.PARENT_ID";
//		List<String> list=new ArrayList<String>();		
//		list=jdbcTemplate.queryForList(sql,java.lang.String.class);	
//				
//		StringBuffer sql2=new StringBuffer( "SELECT a.USER_NAME,a.USERID,a.POSITION,a.MOBILE,"
//				+ "CASE WHEN STATUS = '1' THEN '已关注' WHEN STATUS = '4' THEN '未关注' ELSE '已禁用' END STATUS "
//				+ "FROM WECHAT.WECHAT_ADDRESS_BOOK a "
//				+ "where (");
//		int count = 0;
//		for (String dep : list) {
//			count++;
//			if (count!=list.size()) {
//				sql2.append(" a.DEPARTMENT_ID like '%"+dep+"%'"+" or");
//			}else{
//				sql2.append(" a.DEPARTMENT_ID like '%"+dep+"%')");
//			}
//		}
//		sql2.append(" and status in ("+status+") and rownum <=500");
		
		String sql="SELECT distinct a.USER_NAME,a.USERID,a.POSITION,a.MOBILE,"
				+ "CASE WHEN STATUS = '1' THEN '已关注' WHEN STATUS = '4' THEN '未关注' ELSE '已禁用' END STATUS "
				+ "FROM WECHAT.WECHAT_ADDRESS_BOOK a "
				+ "left join  SALES.EMP_POSITION_ORG_VIEW c on c.EMP_ID=a.USERID "
				+ "left join (select w.USERID,w.department_id from wechat.wechat_userid_departmentid w,(select b.DEPARTMENT_ID from WECHAT.WECHAT_CONTACTS_DEPARTMENT b  START WITH b.DEPARTMENT_ID='"+dId+"' CONNECT BY NOCYCLE PRIOR b.DEPARTMENT_ID = b.PARENT_ID) v where w.DEPARTMENT_ID=v.DEPARTMENT_ID)u on u.userid=a.userid "
				+ "where  exists(select 1 from wechat.wechat_userid_departmentid u ,(select bb.DEPARTMENT_ID from WECHAT.WECHAT_CONTACTS_DEPARTMENT bb  START WITH bb.DEPARTMENT_ID='"+dId+"' CONNECT BY NOCYCLE PRIOR bb.DEPARTMENT_ID = bb.PARENT_ID) c where u.department_id=c.DEPARTMENT_ID and a.USERID=u.userid )"
				+ "and status in ("+status+") "
				+ "and rownum <=500";
		
		PageResult page=new PageResult(sql.toString(), currentPage, numPerPage, this.salesJdbcTemplate);
		return page; 
	}
			
	//通过USERID查询导出多个部门中的员工
		@Override
		public List<Emp> queryEmpListByUserid(String[] uIds) {
			// TODO Auto-generated method stub		
			StringBuffer uIdsSB=new StringBuffer();
			for(int i=0;i<uIds.length;i++){
				if(i==uIds.length-1){
					uIdsSB.append("'"+uIds[i]+"'");	 
				 }else{
					 uIdsSB.append("'"+uIds[i]+"',");
				 }
			}
			List<Emp> empList=new ArrayList<Emp>(); 
			String sql="select "
					+"(SELECT COMPANY_NAME from SALES.EMP_POSITION_ORG_VIEW where EMP_ID=a.USERID) COMPANY_NAME,"
					+"(SELECT BRANCH_NAME from SALES.EMP_POSITION_ORG_VIEW where EMP_ID=a.USERID) BRANCH_NAME,"
					+ "a.USER_NAME,a.USERID,a.POSITION,a.MOBILE,"
	//				+ "(SELECT DEPT_NAME from WECHAT.WECHAT_CONTACTS_DEPARTMENT where DEPARTMENT_ID=WECHAT.WECHAT_ADDRESS_BOOK.DEPARTMENT_ID) DEPT_NAME,"
					+"(select listagg(DEPT_NAME,'－') within GROUP(order by level desc) "
					+"from WECHAT.WECHAT_CONTACTS_DEPARTMENT "
					+"start with department_id = (select b.department_id from WECHAT.WECHAT_ADDRESS_BOOK b where b.userid=a.USERID) "
					+"connect by prior parent_id=department_id) DEPT_NAME,"					
					+ "CASE "
					+ "WHEN a.STATUS = '1' THEN '已关注' "
					+ "WHEN a.STATUS = '4' THEN '未关注' "
					+ "ELSE '已禁用' END STATUS "
					+ "from WECHAT.WECHAT_ADDRESS_BOOK a "
					+"where a.USERID in("+uIdsSB.toString()+")";
			empList = wechatJdbcTemplate.query(sql , new RowMapper<Emp>(){
		        @Override
		        public Emp mapRow(ResultSet rs, int index) throws SQLException {       
		        	Emp emp = new Emp();
		        	emp.setCompanyName(rs.getString("COMPANY_NAME"));
		        	emp.setBranchName(rs.getString("BRANCH_NAME"));
		        	emp.setuName(rs.getString("USER_NAME"));
		        	emp.setuId(rs.getString("USERID"));
		        	emp.setPosition(rs.getString("POSITION"));
		        	emp.setMobile(rs.getString("MOBILE"));
		        	emp.setdId(rs.getString("DEPT_NAME"));	
		        	emp.setStatus(rs.getString("STATUS"));	  	
		        	return emp;
		        };
			});
			return empList;
		}
		
		//通过USERID分页查询导出多个部门中的员工
		@Override
		public PageResult queryEmpByUseridPage(Integer currentPage, Integer numPerPage, String[] uIds) {
			StringBuffer uIdsSB=new StringBuffer();
			for(int i=0;i<uIds.length;i++){
				if(i==uIds.length-1){
					uIdsSB.append("'"+uIds[i]+"'");	 
				 }else{
					 uIdsSB.append("'"+uIds[i]+"',");
				 }
			}
			String sql="select "
					+"(SELECT COMPANY_NAME from SALES.EMP_POSITION_ORG_VIEW where EMP_ID=a.USERID) COMPANY_NAME,"
					+"(SELECT BRANCH_NAME from SALES.EMP_POSITION_ORG_VIEW where EMP_ID=a.USERID) BRANCH_NAME,"
					+ "a.USER_NAME,a.USERID,a.POSITION,a.MOBILE,"
					+"(select listagg(DEPT_NAME,'－') within GROUP(order by level desc) "
					+"from WECHAT.WECHAT_CONTACTS_DEPARTMENT "
					+"start with department_id = (select b.department_id from WECHAT.WECHAT_ADDRESS_BOOK b where b.userid=a.USERID) "
					+"connect by prior parent_id=department_id) DEPT_NAME,"					
					+ "CASE "
					+ "WHEN a.STATUS = '1' THEN '已关注' "
					+ "WHEN a.STATUS = '4' THEN '未关注' "
					+ "ELSE '已禁用' END STATUS "
					+ "from WECHAT.WECHAT_ADDRESS_BOOK a "
					+"where (a.USERID in("+uIdsSB.toString()+")) "
					+ "and rownum <=800";
			PageResult page=new PageResult(sql, currentPage, numPerPage, this.wechatJdbcTemplate);
			return page; 
		}
	
		
		//登录
		@Override
		public boolean login(String loginId) {
			// TODO Auto-generated method stub
			boolean  flag=false;
			if(loginId==null){
				flag=false;
			}else{
				String sql="select 1 from WECHAT.WECHAT_ADDRESS_BOOK where USERID='"+loginId+"' ";
				List<String> str=wechatJdbcTemplate.queryForList(sql,String.class);
				if(str.size()>0){
					flag=true;
				}
			}			
			return flag;
		}
		
		
		//判断用户名的正确性
		@Override
		public boolean uIdJudge(Emp emp) {
			// TODO Auto-generated method stub
			boolean  flag=false;
			String sql="select 1 from WECHAT.WECHAT_ADDRESS_BOOK where USERID='"+emp.getuId()+"'";
			try{
				List<String> str=wechatJdbcTemplate.queryForList(sql,java.lang.String.class);
				for (String str2 : str) {				
					if(str2.equals("1")){
						flag=true;			
					}				
				}			
			}catch(EmptyResultDataAccessException e){
				return flag;
			}
			return flag;
		}
		

}
