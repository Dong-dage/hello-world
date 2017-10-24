package com.want.wx.service.impl;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.want.wx.dao.UserDao;
import com.want.wx.dao.impl.EmpDaoImpl;
import com.want.wx.dao.impl.UserDaoImpl;
import com.want.wx.pojo.Emp;
import com.want.wx.pojo.User;
import com.want.wx.service.UserService;
import com.want.wx.util.PageResult;


@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;

	//查询标签中的User	
	@Override
	public List<User> queryUserById(String status) {
		// TODO Auto-generated method stub
		return userDao.queryUserById(status);
	}
	
	//通过userId导出大数据的execl
	@Override
	public void exportByUserId(HttpServletResponse response,String[] userIds,String status) {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	    OutputStream os = null; 
	    SXSSFWorkbook xWorkbook = null;
	    try {
	            String fileName = "User" + df.format(new Date()) + ".xlsx";
	            
	            os = response.getOutputStream();
	            response.reset();
	            
	            response.setHeader("Content-disposition", "attachment; filename = " + URLEncoder.encode(fileName, "UTF-8"));
	            response.setContentType("application/octet-streem");
	            
	            List<User> userList = userDao.exportByUserId(userIds,status);
	                        
	            if (null != userList && userList.size() > 0) {
	            	int rowaccess=100;
	            	int totle=userList.size();
	            	int mus=20000;
	            	int avg=totle/mus;
	            	xWorkbook = new SXSSFWorkbook(rowaccess);
                    CellStyle cs = xWorkbook.createCellStyle();
	                for (int i = 0; i < avg+1; i++) {  
	                	//set Sheet页名称
	                	Sheet sh = xWorkbook.createSheet("UserList"+(i+1));
	                	//set Sheet页头部
	                    setSheetHeader(xWorkbook, sh);
	                    int num=i*mus;
	                    int index=0;
	                    for(int m=num;m<totle;m++){
	                    	if(index==mus){
	                    		break;
	                    	}
	                    	Row xRow = sh.createRow(m + 1-mus*i);
	                        User user = userList.get(m);
	                        for (int j = 0; j < 8; j++) {
	                            Cell xCell = xRow.createCell(j);
	                            
	                            cs.setWrapText(true);  //是否自动换行
	                            xCell.setCellStyle(cs);
	                            
	                            switch (j) {
	                            	case 0:
	        	                      	xCell.setCellValue(user.getCompany_name());
	        	                          break;                           
	        	                    case 1:
	        	                      	xCell.setCellValue(user.getChannel_name());
	        	                          break;                                                 
	                                case 2:
	                                	xCell.setCellValue(user.getName());
	                                    break;                           
	                                case 3:
	                                	xCell.setCellValue(user.getUserid());
	                                    break;
	                                case 4:
	                                    xCell.setCellValue(user.getPosition());
	                                    break;
	                                case 5:
	                                	xCell.setCellValue(user.getMobile());
	                                    break;
	                                case 6:
	                                    xCell.setCellValue(user.getDepartment());
	                                    break;
	                                case 7:
	                                    xCell.setCellValue(user.getStatus());
	                                    break;
	                                default:
	                                    break;
	                            }
	                        } 
	                        index++;
	                        //每当行数达到设置的值就刷新数据到硬盘,以清理内存
	                        if(m%rowaccess==0){
	                        	((SXSSFSheet)sh).flushRows();
	                        }
	                    }                 
	                }        
	                xWorkbook.write(os);
	            }                                  
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (null != os) {
	                try {
	                    os.close();
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }           
	            if (null != xWorkbook) {
	                try {
	                    xWorkbook.close();
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        
	    }
	    /**
	     * set Sheet页头部
	     */
	    @SuppressWarnings("deprecation")
		private void setSheetHeader(SXSSFWorkbook xWorkbook, Sheet sh) {           
	    	sh.setColumnWidth(0, 20 * 156);
	    	sh.setColumnWidth(1, 20 * 156);
	    	sh.setColumnWidth(2, 20 * 156);
	    	sh.setColumnWidth(3, 20 * 156);
	    	sh.setColumnWidth(4, 20 * 156);
	    	sh.setColumnWidth(5, 20 * 156);
	    	sh.setColumnWidth(6, 20 * 256);
	    	sh.setColumnWidth(7, 20 * 156);
	    	   	
	        CellStyle cs = xWorkbook.createCellStyle();
	        //设置水平垂直居中
	        cs.setAlignment(CellStyle.ALIGN_CENTER);
	        cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	        //设置字体
	        Font headerFont = xWorkbook.createFont();
	        headerFont.setFontHeightInPoints((short) 12);
	        headerFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
	        headerFont.setFontName("宋体");
	        cs.setFont(headerFont);
	        cs.setWrapText(true);//是否自动换行
	        
	        Row xRow0 = sh.createRow(0);
	        
	        Cell xCell0 = xRow0.createCell(0);
	        xCell0.setCellStyle(cs);
	        xCell0.setCellValue("分公司");
	        
	        Cell xCell1 = xRow0.createCell(1);
	        xCell1.setCellStyle(cs);
	        xCell1.setCellValue("营业所");
	        
	        Cell xCell2 = xRow0.createCell(2);
	        xCell2.setCellStyle(cs);
	        xCell2.setCellValue("姓名");    
	        
	        Cell xCell3 = xRow0.createCell(3);
	        xCell3.setCellStyle(cs);
	        xCell3.setCellValue("帐号");
	        
	        Cell xCell4 = xRow0.createCell(4);
	        xCell4.setCellStyle(cs);
	        xCell4.setCellValue("职位");
	        
	        Cell xCell5 = xRow0.createCell(5);
	        xCell5.setCellStyle(cs);
	        xCell5.setCellValue("手机"); 
	        
	        Cell xCell6 = xRow0.createCell(6);
	        xCell6.setCellStyle(cs);
	        xCell6.setCellValue("组织");
	        
	        Cell xCell7 = xRow0.createCell(7);
	        xCell7.setCellStyle(cs);
	        xCell7.setCellValue("状态");
	    }

    
	//通过临时表导出Excel
	@Override
	public void exportByPermUserId(HttpServletResponse response, String status){
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
			OutputStream os = null;
			SXSSFWorkbook xWorkbook = null;
			try{
				String fileName = "User" + sf.format(new Date()) + ".xlsx";
				os = response.getOutputStream();
				response.reset();
				response.setHeader("Content-disposition", "attachment; filename =" + URLEncoder.encode(fileName, "UTF-8"));
				response.setContentType("application/octet-streem");
				
				List<User> userList =userDao.exportByPermUserId(status);
				
				if(userList != null && userList.size()>0){
					int rowaccess = 100;
					int totle = userList.size();
					int mus = 20000;
					int avg = totle/mus;
					xWorkbook = new SXSSFWorkbook(rowaccess);
					CellStyle cs = xWorkbook.createCellStyle();
					for(int i = 0;i<avg+1;i++){
						//set Sheet页名称
						Sheet sh = xWorkbook.createSheet("UserList"+(i+1));
						//set Sheet页头部
						setSheetHeader1(xWorkbook, sh);
						int num = i*mus;
						int index = 0;
						for(int m=num;m<totle;m++){
							if(index==mus){
								break;
							}
							Row xRow = sh.createRow(m+1-mus*i);
							User user = userList.get(m);
							for(int j = 0;j < 8;j++){
								Cell xCell = xRow.createCell(j);
								
								switch(j){
								  case 0:
			                      	xCell.setCellValue(user.getCompany_name());
			                          break;                           
			                      case 1:
			                      	xCell.setCellValue(user.getChannel_name());
			                          break;                           
			                      case 2:
			                      	xCell.setCellValue(user.getName());
			                          break;                           
			                      case 3:
			                      	xCell.setCellValue(user.getUserid());
			                          break;
			                      case 4:
			                          xCell.setCellValue(user.getPosition());
			                          break;
			                      case 5:
			                      	xCell.setCellValue(user.getMobile());
			                          break;
			                      case 6:
			                          xCell.setCellValue(user.getDepartment());
			                          break;
			                      case 7:
			                          xCell.setCellValue(user.getStatus());
			                          break;
			                      default:
			                          break;
								}
							}
							index++;
							//当行数达到设置的值就刷新数据到硬盘，清理内存
							if(m%rowaccess==0){
								((SXSSFSheet)sh).flushRows();
							}
						}
					}
		            xWorkbook.write(os);
				}
		
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(null !=os){
					try{
						os.close();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				if(xWorkbook != null){
					try{
						xWorkbook.close();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
	}
	/**
     * set Sheet页头部
     */
    @SuppressWarnings("deprecation")
	private void setSheetHeader1(SXSSFWorkbook xWorkbook, Sheet sh) {           
    	sh.setColumnWidth(0, 20 * 156);
    	sh.setColumnWidth(1, 20 * 156);
    	sh.setColumnWidth(2, 20 * 156);
    	sh.setColumnWidth(3, 20 * 156);
    	sh.setColumnWidth(4, 20 * 156);
    	sh.setColumnWidth(5, 20 * 156);
    	sh.setColumnWidth(6, 20 * 256);
    	sh.setColumnWidth(7, 20 * 156);
    	   	
        CellStyle cs = xWorkbook.createCellStyle();
        //设置水平垂直居中
        cs.setAlignment(CellStyle.ALIGN_CENTER);
        cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        //设置字体
        Font headerFont = xWorkbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        headerFont.setFontName("宋体");
        cs.setFont(headerFont);
        cs.setWrapText(true);//是否自动换行
        
        Row xRow0 = sh.createRow(0);
        
        Cell xCell0 = xRow0.createCell(0);
        xCell0.setCellStyle(cs);
        xCell0.setCellValue("分公司");
        
        Cell xCell1 = xRow0.createCell(1);
        xCell1.setCellStyle(cs);
        xCell1.setCellValue("营业所");
        
        Cell xCell2 = xRow0.createCell(2);
        xCell2.setCellStyle(cs);
        xCell2.setCellValue("姓名");    
        
        Cell xCell3 = xRow0.createCell(3);
        xCell3.setCellStyle(cs);
        xCell3.setCellValue("帐号");
        
        Cell xCell4 = xRow0.createCell(4);
        xCell4.setCellStyle(cs);
        xCell4.setCellValue("职位");
        
        Cell xCell5 = xRow0.createCell(5);
        xCell5.setCellStyle(cs);
        xCell5.setCellValue("手机"); 
        
        Cell xCell6 = xRow0.createCell(6);
        xCell6.setCellStyle(cs);
        xCell6.setCellValue("组织");
        
        Cell xCell7 = xRow0.createCell(7);
        xCell7.setCellStyle(cs);
        xCell7.setCellValue("状态");
    }            		
		
	public void deleteTempUserId(){			
		userDao.deleteTempUserId();
	}
		
	public void saveTempUserId(String userid){			
		userDao.saveTempUserId(userid);
	}

	@Override
	public PageResult queryPage(Integer currentPage, Integer numPerPage,String status) {
		// TODO Auto-generated method stub
		return userDao.queryPage(currentPage, numPerPage,status);
	}
	
}
