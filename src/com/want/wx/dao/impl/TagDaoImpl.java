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

import com.want.wx.dao.TagDao;
import com.want.wx.pojo.Tag;

@Repository
public class TagDaoImpl implements TagDao {
	@Autowired
	@Qualifier("wechatJdbcTemplate")
	public JdbcTemplate wechatJdbcTemplate;
	
	@Override
	public List<Tag> queryTag() {
		
		List<Tag> tagList = new ArrayList<Tag>();
		String sql = "select distinct WECHAT_TAGID,tagname from WECHAT.WECHAT_TAG";
	
		tagList = wechatJdbcTemplate.query(sql, new RowMapper<Tag>(){
			@Override
			public Tag mapRow(ResultSet rs,int index) throws SQLException{
				Tag tag = new Tag();
				tag.setTagname(rs.getString("tagname"));
				tag.setWechat_tagid(rs.getString("wechat_tagid"));
				return tag;
			};
		});
		
		return tagList;
	}

	@Override
	public List<Tag> queryTagList(List agentIdList) {
		// TODO Auto-generated method stub
		List<Tag> tagList=new ArrayList<Tag>();
		StringBuffer sql=new StringBuffer();
		if (agentIdList.size()>0) {
			sql.append("select distinct tag_id,tag_name from wechat.wechat_agent_tag where agent_id in( " );
			int count = 0;
			for (Object  agentId : agentIdList) {			
				count++;			
				if (count!=agentIdList.size()) {
					sql.append(" '"+agentId+"' ,");
				}else{
					sql.append("'"+agentId+"'");
				}
			}					
			sql.append(" ) ");
			
			tagList = wechatJdbcTemplate.query(sql.toString(), new RowMapper<Tag>(){			 
		        @Override
		        public Tag mapRow(ResultSet rs, int index) throws SQLException {            
		        	Tag tag = new Tag();		        	
		        	tag.setWechat_tagid(rs.getString("tag_id"));
		        	tag.setTagname(rs.getString("tag_name"));
		            return tag;		            
		        };
			});							
		}				
		return tagList;
	}
	
}
