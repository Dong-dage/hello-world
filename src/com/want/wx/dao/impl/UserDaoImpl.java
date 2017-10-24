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

import com.want.wx.dao.UserDao;
import com.want.wx.pojo.User;
import com.want.wx.util.PageResult;

@Repository
public class UserDaoImpl implements UserDao {
	
	@Autowired
	@Qualifier("wechatJdbcTemplate")
	public JdbcTemplate wechatJdbcTemplate;
	@Autowired
	@Qualifier("salesJdbcTemplate")
	public JdbcTemplate salesJdbcTemplate;
	
	//查询显示标签中的员工
	@Override
	public List<User> queryUserById(String status) {
		// TODO Auto-generated method stub
		List<User> userList = new ArrayList<User>();		
		String sql = " select a.USER_NAME,a.USERID,a.POSITION,a.MOBILE,"			
				+ " CASE WHEN a.STATUS = '1' THEN '已关注' WHEN a.STATUS = '4' THEN '未关注' ELSE '已禁用' END STATUS "
				+ " from WECHAT.WECHAT_ADDRESS_BOOK a "
				//+ " left join  SALES.EMP_POSITION_ORG_VIEW c on c.EMP_ID=a.USERID "
				//+ " left join (select w.USERID,w.department_id from wechat.wechat_userid_departmentid w where userid in(select m.userid from WECHAT.WECHAT_TEMP_USERID m) )u on u.userid=a.userid "
				+ " where (a.USERID in(select n.userid from WECHAT.WECHAT_TEMP_USERID n)) "
				+ " and status in ("+status+") "
				+" and rownum <=800";
							       			       
		//System.out.println(sql);
		userList = salesJdbcTemplate.query(sql, new RowMapper<User>(){
			public User mapRow(ResultSet rs, int index) throws SQLException{
				User user =  new User();
				user.setCompany_name("");
				user.setChannel_name("");	
				user.setName(rs.getString("USER_NAME"));
				user.setUserid(rs.getString("USERID"));
				user.setPosition(rs.getString("POSITION"));
				user.setMobile(rs.getString("MOBILE"));
				user.setDepartment("");
				user.setStatus(rs.getString("STATUS"));
				return user;
			}
		});
		return userList;
	}
	
	//通过USERID导出多个标签中的员工
	@Override
	public List<User> exportByUserId(String[] userIds,String status) {
		// TODO Auto-generated method stub
		StringBuffer uIdsSB = new StringBuffer();
		for (int i = 0; i < userIds.length; i++) {
			if (i == userIds.length - 1) {
				uIdsSB.append("'" + userIds[i] + "'");
			} else {
				uIdsSB.append("'" + userIds[i] + "',");
			}
		}
		List<User> userList = new ArrayList<User>();
		String sql = " select c.COMPANY_NAME,c.BRANCH_NAME,a.USER_NAME,a.USERID,a.POSITION,a.MOBILE,"
				+ " (select listagg(DEPT_NAME,'－') within GROUP(order by level desc) from WECHAT.WECHAT_CONTACTS_DEPARTMENT start with department_id = u.department_id CONNECT BY prior parent_id=department_id) DEPT_NAME,"
				+ " CASE WHEN a.STATUS = '1' THEN '已关注' WHEN a.STATUS = '4' THEN '未关注' ELSE '已禁用' END STATUS "
				+ " from WECHAT.WECHAT_ADDRESS_BOOK a "
				+ " left join  SALES.EMP_POSITION_ORG_VIEW c on c.EMP_ID=a.USERID "
				+ " left join (select w.USERID,w.department_id from wechat.wechat_userid_departmentid w where userid in("+uIdsSB.toString()+") )u on u.userid=a.userid "
				+ " where (a.USERID in("+uIdsSB.toString()+")) "
				+ " and status in ("+status+") ";
		
		
		/*List<User> userList = new ArrayList<User>();
		for (int i = 0; i < userIds.length; i++) {
			//System.out.println(userIds[i]);
						
			String sql = " select c.COMPANY_NAME,c.BRANCH_NAME,a.USER_NAME,a.USERID,a.POSITION,a.MOBILE,"
					+ " (select listagg(DEPT_NAME,'－') within GROUP(order by level desc) from WECHAT.WECHAT_CONTACTS_DEPARTMENT start with department_id = u.department_id CONNECT BY prior parent_id=department_id) DEPT_NAME,"
					+ " CASE WHEN a.STATUS = '1' THEN '已关注' WHEN a.STATUS = '4' THEN '未关注' ELSE '已禁用' END STATUS "
					+ " from WECHAT.WECHAT_ADDRESS_BOOK a "
					+ " left join  SALES.EMP_POSITION_ORG_VIEW c on c.EMP_ID=a.USERID "
					+ " left join (select w.USERID,w.department_id from wechat.wechat_userid_departmentid w where exists(w.userid='"+userIds[i]+"'))u on u.userid=a.userid "
					+ " where exists(a.USERID='"+userIds[i]+"') "
					+ " and status in ("+status+") ";*/
			
			userList = salesJdbcTemplate.query(sql, new RowMapper<User>() {
				@Override
				public User mapRow(ResultSet rs, int index) throws SQLException {
					User user = new User();
					user.setCompany_name(rs.getString("COMPANY_NAME"));
					user.setChannel_name(rs.getString("BRANCH_NAME"));
					user.setName(rs.getString("USER_NAME"));
					user.setUserid(rs.getString("USERID"));
					user.setPosition(rs.getString("POSITION"));
					user.setMobile(rs.getString("MOBILE"));
					user.setDepartment(rs.getString("DEPT_NAME"));
					user.setStatus(rs.getString("STATUS"));
					return user;
				};
			});
		return userList;
	}
		
	//通过UserId临时表查询导出多个标签中的员工
	@Override
	public List<User> exportByPermUserId(String status) {
		List<User> userList = new ArrayList<User>();
		
		/*String sql ="";
			sql+=" SELECT c.COMPANY_NAME,c.BRANCH_NAME,a.USER_NAME,a.USERID,a.POSITION,a.MOBILE,"
							+ "replace(wm_concat(d.DEPT_NAME),',',';') DEPT_NAME, "
							//+ "(SELECT listagg(DEPT_NAME,'－') within GROUP(ORDER BY level DESC) FROM WECHAT.WECHAT_CONTACTS_DEPARTMENT START WITH department_id = (SELECT b.department_id FROM WECHAT.WECHAT_ADDRESS_BOOK b WHERE b.userid =a.userid) CONNECT BY prior parent_id=department_id) DEPT_NAME,"
							+ "CASE WHEN a.STATUS = '1' THEN '已关注' WHEN a.STATUS = '4' THEN '未关注' ELSE '已禁用' END STATUS "
							+ "FROM WECHAT.WECHAT_ADDRESS_BOOK a "
							+ "left join  SALES.EMP_POSITION_ORG_VIEW c on c.EMP_ID=a.USERID "
							+ "left join WECHAT.WECHAT_CONTACTS_DEPARTMENT d on  instr(REPLACE(a.DEPARTMENT_ID,';',','),d.DEPARTMENT_ID) > 0 "
							+ "where a.userid in (select d.user_id from WECHAT.TEST_WECHAT_USER d ) "
							+ "and status in ("+status+") "
							+ " group by c.COMPANY_NAME,c.BRANCH_NAME,a.USER_NAME,a.USERID,a.POSITION,a.MOBILE,a.STATUS ";
*/
		/*String sql = " select c.COMPANY_NAME,c.BRANCH_NAME,"
				+ " a.USER_NAME,a.USERID,a.POSITION,a.MOBILE,"
				+ " (select listagg(DEPT_NAME,'－') within GROUP(order by level desc) from WECHAT.WECHAT_CONTACTS_DEPARTMENT start with department_id = u.department_id CONNECT BY prior parent_id=department_id) DEPT_NAME,"
				+ " CASE WHEN a.STATUS = '1' THEN '已关注' WHEN a.STATUS = '4' THEN '未关注' ELSE '已禁用' END STATUS "
				+ " from WECHAT.WECHAT_ADDRESS_BOOK a "
				+ " left join  SALES.EMP_POSITION_ORG_VIEW c on c.EMP_ID=a.USERID "
				+ " left join (select w.USERID,w.department_id from wechat.wechat_userid_departmentid w where userid in(select m.userid from WECHAT.WECHAT_TEMP_USERID m) )u on u.userid=a.userid "
				+ " where (a.USERID in(select n.userid from WECHAT.WECHAT_TEMP_USERID n)) "
				+ " and status in ("+status+") ";
		*/	
		
		/**
		 * sql中的COMPANY_NAME做处理的目的是为了解决一人兼职多岗时出现异常
		 * 
		 * 例如出现下面错误类型的数据
		 * 
		 * 分公司	   营业所	 姓名	    工号			职位		组织
		 * 杭州分	(null)	万世强	00004158	副理	旺旺集团－现代渠道发展营业部－现渠杭州分公司
		 * 郑州分	(null)	万世强	00004158	副理	旺旺集团－现代渠道发展营业部－现渠杭州分公司
		 * 郑州分	(null)	万世强	00004158	副理	旺旺集团－现代渠道发展营业部－现渠郑州分公司
		 * 杭州分	(null)	万世强	00004158	副理	旺旺集团－现代渠道发展营业部－现渠郑州分公司		
		 */
		
		
		String sql = " select distinct "
				//+ " c.COMPANY_NAME,"
				+ " CASE WHEN  instr((select listagg(DEPT_NAME,'－') within GROUP(order by level desc) from WECHAT.WECHAT_CONTACTS_DEPARTMENT "
				+ "				start with department_id = u.department_id CONNECT BY prior parent_id=department_id),'分公司') > 0 "
				+ "			and instr((select listagg(DEPT_NAME,'－') within GROUP(order by level desc) from WECHAT.WECHAT_CONTACTS_DEPARTMENT "
				+ "				start with department_id = u.department_id CONNECT BY prior parent_id=department_id),'－分公司－') < 1"
				+ "			and (select count(w.department_id) from wechat.wechat_userid_departmentid w where  userid=a.userid) > 1 "
				+ "			and (select count(distinct COMPANY_NAME) from SALES.EMP_POSITION_ORG_VIEW c where c.EMP_ID=a.USERID)>1 "
				+ "			and length(a.userid) <> 10 "
				+ " 	THEN substr(	(select listagg(DEPT_NAME,'－') within GROUP(order by level desc) from WECHAT.WECHAT_CONTACTS_DEPARTMENT  "
                + "         	start with department_id = u.department_id CONNECT BY prior parent_id=department_id), "
                + " 		instr((select listagg(DEPT_NAME,'－') within GROUP(order by level desc) from WECHAT.WECHAT_CONTACTS_DEPARTMENT " 
                + "         	start with department_id = u.department_id CONNECT BY prior parent_id=department_id),'现渠')+2, "
                + " 		instr((select listagg(DEPT_NAME,'－') within GROUP(order by level desc) from WECHAT.WECHAT_CONTACTS_DEPARTMENT " 
                + "         	start with department_id = u.department_id CONNECT BY prior parent_id=department_id),'公司') "                      
                + "  		-instr((select listagg(DEPT_NAME,'－') within GROUP(order by level desc) from WECHAT.WECHAT_CONTACTS_DEPARTMENT " 
                + "         	start with department_id = u.department_id CONNECT BY prior parent_id=department_id),'现渠')-2 "        
                + " 	) "
                + "		WHEN  instr((select listagg(DEPT_NAME,'－') within GROUP(order by level desc) from WECHAT.WECHAT_CONTACTS_DEPARTMENT "
				+ "				start with department_id = u.department_id CONNECT BY prior parent_id=department_id),'分公司') > 0 "
				+ "			and instr((select listagg(DEPT_NAME,'－') within GROUP(order by level desc) from WECHAT.WECHAT_CONTACTS_DEPARTMENT "
				+ "				start with department_id = u.department_id CONNECT BY prior parent_id=department_id),'－分公司－') < 1"
				+ "			and (select count(distinct COMPANY_NAME) from SALES.EMP_POSITION_ORG_VIEW c where c.EMP_ID=a.USERID) < 1 "
				+ "			and length(a.userid) <> 10 "
				+ " 	THEN substr(	(select listagg(DEPT_NAME,'－') within GROUP(order by level desc) from WECHAT.WECHAT_CONTACTS_DEPARTMENT  "
                + "         	start with department_id = u.department_id CONNECT BY prior parent_id=department_id), "
                + " 		instr((select listagg(DEPT_NAME,'－') within GROUP(order by level desc) from WECHAT.WECHAT_CONTACTS_DEPARTMENT " 
                + "         	start with department_id = u.department_id CONNECT BY prior parent_id=department_id),'现渠')+2, "
                + " 		instr((select listagg(DEPT_NAME,'－') within GROUP(order by level desc) from WECHAT.WECHAT_CONTACTS_DEPARTMENT " 
                + "         	start with department_id = u.department_id CONNECT BY prior parent_id=department_id),'公司') "                      
                + "  		-instr((select listagg(DEPT_NAME,'－') within GROUP(order by level desc) from WECHAT.WECHAT_CONTACTS_DEPARTMENT " 
                + "         	start with department_id = u.department_id CONNECT BY prior parent_id=department_id),'现渠')-2 "        
                + " 	) "
                + "		WHEN a.userid like '00%' and length(a.userid) = 10 "
                + "		THEN cpv.COMPANY_NAME "              
                + " ELSE c.COMPANY_NAME END COMPANY_NAME,"
//				+ " c.BRANCH_NAME,"
				+ "  CASE WHEN instr((select listagg(DEPT_NAME,'－') within GROUP(order by level desc) from WECHAT.WECHAT_CONTACTS_DEPARTMENT "
				+ " 			start with department_id = u.department_id CONNECT BY prior parent_id=department_id),c.BRANCH_NAME) > 0 "
				+ " 		and length(a.userid) = 8"
				+ " 	THEN c.BRANCH_NAME"
				+ " 	WHEN  a.userid like '00%' and length(a.userid) = 10 "
				+ " 	THEN cpv.BRANCH_NAME "
				+ " ELSE '' END BRANCH_NAME,"
				+ " a.USER_NAME,a.USERID,a.POSITION,a.MOBILE,"
				+ " (select listagg(DEPT_NAME,'－') within GROUP(order by level desc) from WECHAT.WECHAT_CONTACTS_DEPARTMENT start with department_id = u.department_id CONNECT BY prior parent_id=department_id) DEPT_NAME,"
				+ " CASE WHEN a.STATUS = '1' THEN '已关注' WHEN a.STATUS = '4' THEN '未关注' ELSE '已禁用' END STATUS "
				+ " from WECHAT.WECHAT_ADDRESS_BOOK a "
				+ " left join sales.customer_property_view cpv on cpv.CUSTOMER_ID=a.userid "
				+ " left join  SALES.EMP_POSITION_ORG_VIEW c on c.EMP_ID=a.USERID "
				+ " left join (select w.USERID,w.department_id from wechat.wechat_userid_departmentid w where userid in(select m.userid from WECHAT.WECHAT_TEMP_USERID m) )u on u.userid=a.userid "
				+ " where (a.USERID in(select n.userid from WECHAT.WECHAT_TEMP_USERID n)) "
				+ " and status in ("+status+") ";
		
				       
		//System.out.println(sql);
		userList = salesJdbcTemplate.query(sql, new RowMapper<User>(){
			public User mapRow(ResultSet rs, int index) throws SQLException{
				User user =  new User();
				user.setCompany_name(rs.getString("COMPANY_NAME"));
				user.setChannel_name(rs.getString("BRANCH_NAME"));	
				user.setName(rs.getString("USER_NAME"));
				user.setUserid(rs.getString("USERID"));
				user.setPosition(rs.getString("POSITION"));
				user.setMobile(rs.getString("MOBILE"));
				user.setDepartment(rs.getString("DEPT_NAME"));
				user.setStatus(rs.getString("STATUS"));
				return user;
			}
		});
		return userList.size()>0?userList:null;
	}	 
		
	@Override
	public void deleteTempUserId(){			
		String sql="delete from WECHAT.WECHAT_TEMP_USERID";
		wechatJdbcTemplate.execute(sql);
	}
		
	@Override
	public void saveTempUserId(String userid){
		String sql="insert  into WECHAT.WECHAT_TEMP_USERID(USERID) values('"+userid+"')";
		wechatJdbcTemplate.execute(sql);		
	}
	
	//分页查询标签中的员工
	@Override
	public PageResult queryPage(Integer currentPage, Integer numPerPage,String status) {
			// TODO Auto-generated method stub		

			String sql=" select a.USER_NAME,a.USERID,a.POSITION,a.MOBILE,"			
					+ " CASE WHEN a.STATUS = '1' THEN '已关注' WHEN a.STATUS = '4' THEN '未关注' ELSE '已禁用' END STATUS "
					+ " from WECHAT.WECHAT_ADDRESS_BOOK a "
					//+ " left join  SALES.EMP_POSITION_ORG_VIEW c on c.EMP_ID=a.USERID "
					//+ " left join (select w.USERID,w.department_id from wechat.wechat_userid_departmentid w where userid in(select m.userid from WECHAT.WECHAT_TEMP_USERID m) )u on u.userid=a.userid "
					+ " where (a.USERID in(select n.userid from WECHAT.WECHAT_TEMP_USERID n)) "
					+ "and rownum <=500";
			
			PageResult page=new PageResult(sql.toString(), currentPage, numPerPage, this.salesJdbcTemplate);
			return page; 
	}	
	
	
}
