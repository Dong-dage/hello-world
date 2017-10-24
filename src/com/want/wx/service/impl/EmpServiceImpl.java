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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.want.wx.dao.EmpDao;
import com.want.wx.pojo.Emp;
import com.want.wx.service.EmpService;
import com.want.wx.util.PageResult;

@Service
public class EmpServiceImpl implements EmpService{
	@Autowired
	private EmpDao dao;

	@Override
	public List<Emp> queryEmpList(String dId) {
		// TODO Auto-generated method stub
		return dao.queryEmpList(dId);
	}
	
	
	//通过Department_id导出execl
//	@Override
//	public void exportFileBydId(HttpServletResponse response,String[] dIds,String status) {
//		// TODO Auto-generated method stub
//		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
//        OutputStream os = null;
//        XSSFWorkbook xWorkbook = null;
//        try {
//            String fileName = "User" + df.format(new Date()) + ".xlsx";
//            
//            os = response.getOutputStream();
//            response.reset();
//            
//            response.setHeader("Content-disposition", "attachment; filename = " + URLEncoder.encode(fileName, "UTF-8"));
//            response.setContentType("application/octet-streem");
//            
//            xWorkbook = new XSSFWorkbook();
//            XSSFSheet xSheet = xWorkbook.createSheet("UserList");
//            
//            //set Sheet页头部
//            setSheetHeader(xWorkbook, xSheet);
//            
//            //set Sheet页内容
//            setSheetContent(xWorkbook, xSheet, dIds,status);
//                       
//            xWorkbook.write(os);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (null != os) {
//                try {
//                    os.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }           
//            if (null != xWorkbook) {
//                try {
//                    xWorkbook.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        
//    }
//    /**
//     * set Sheet页头部
//     */
//    @SuppressWarnings("deprecation")
//	private void setSheetHeader(XSSFWorkbook xWorkbook, XSSFSheet xSheet) {           
//    	xSheet.setColumnWidth(0, 20 * 156);
//        xSheet.setColumnWidth(1, 20 * 156);
//        xSheet.setColumnWidth(2, 20 * 156);
//        xSheet.setColumnWidth(3, 20 * 156);
//        xSheet.setColumnWidth(4, 20 * 156);
//        xSheet.setColumnWidth(5, 20 * 156);
//        xSheet.setColumnWidth(6, 20 * 256);
//        xSheet.setColumnWidth(7, 20 * 156);
//    	   	
//        CellStyle cs = xWorkbook.createCellStyle();
//        //设置水平垂直居中
//        cs.setAlignment(CellStyle.ALIGN_CENTER);
//        cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//        //设置字体
//        Font headerFont = xWorkbook.createFont();
//        headerFont.setFontHeightInPoints((short) 12);
//        headerFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
//        headerFont.setFontName("宋体");
//        cs.setFont(headerFont);
//        cs.setWrapText(true);//是否自动换行
//        
//        XSSFRow xRow0 = xSheet.createRow(0);
//        
//        XSSFCell xCell0 = xRow0.createCell(0);
//        xCell0.setCellStyle(cs);
//        xCell0.setCellValue("分公司");
//        
//        XSSFCell xCell1 = xRow0.createCell(1);
//        xCell1.setCellStyle(cs);
//        xCell1.setCellValue("营业所");
//        
//        XSSFCell xCell2 = xRow0.createCell(2);
//        xCell2.setCellStyle(cs);
//        xCell2.setCellValue("姓名");    
//        
//        XSSFCell xCell3 = xRow0.createCell(3);
//        xCell3.setCellStyle(cs);
//        xCell3.setCellValue("帐号");
//        
//        XSSFCell xCell4 = xRow0.createCell(4);
//        xCell4.setCellStyle(cs);
//        xCell4.setCellValue("职位");
//        
//        XSSFCell xCell5 = xRow0.createCell(5);
//        xCell5.setCellStyle(cs);
//        xCell5.setCellValue("手机"); 
//        
//        XSSFCell xCell6 = xRow0.createCell(6);
//        xCell6.setCellStyle(cs);
//        xCell6.setCellValue("组织");
//        
//        XSSFCell xCell7 = xRow0.createCell(7);
//        xCell7.setCellStyle(cs);
//        xCell7.setCellValue("状态");
//    }
//    /**
//     * set Sheet页内容
//     */
//    private void setSheetContent(XSSFWorkbook xWorkbook, XSSFSheet xSheet,String[] dIds,String status) {
//        List<Emp> empList = dao.queryEmpListBydId(dIds,status);
//        System.out.println(empList.size());
//        CellStyle cs = xWorkbook.createCellStyle();
//        cs.setWrapText(true);
//        
//        if (null != empList && empList.size() > 0) {
//            for (int i = 0; i < empList.size(); i++) {
//                XSSFRow xRow = xSheet.createRow(i + 1);
//                Emp emp = empList.get(i);
//                for (int j = 0; j < 8; j++) {
//                    XSSFCell xCell = xRow.createCell(j);
//                    xCell.setCellStyle(cs);
//                    switch (j) {
//                    	case 0:
//	                      	xCell.setCellValue(emp.getCompanyName());
//	                          break;                           
//	                    case 1:
//	                      	xCell.setCellValue(emp.getBranchName());
//	                          break;                                                 
//                        case 2:
//                        	xCell.setCellValue(emp.getuName());
//                            break;                           
//                        case 3:
//                        	xCell.setCellValue(emp.getuId());
//                            break;
//                        case 4:
//                            xCell.setCellValue(emp.getPosition());
//                            break;
//                        case 5:
//                        	xCell.setCellValue(emp.getMobile());
//                            break;
//                        case 6:
//                            xCell.setCellValue(emp.getdId());
//                            break;
//                        case 7:
//                            xCell.setCellValue(emp.getStatus());
//                            break;
//                        default:
//                            break;
//                    }
//                }    
//            }            
//        }
//	}
    
    
  //通过userid导出execl
  	@Override
  	public void exportFileByUserid(HttpServletResponse response,String[] uIds) {
  		// TODO Auto-generated method stub
  		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
          OutputStream os = null;
          XSSFWorkbook xWorkbook = null;
          try {
              String fileName = "User" + df.format(new Date()) + ".xlsx";
              
              os = response.getOutputStream();
              response.reset();
              
              response.setHeader("Content-disposition", "attachment; filename = " + URLEncoder.encode(fileName, "UTF-8"));
              response.setContentType("application/octet-streem");
              
              xWorkbook = new XSSFWorkbook();
              XSSFSheet xSheet = xWorkbook.createSheet("UserList");
              
              //set Sheet页头部
              setSheetHeader1(xWorkbook, xSheet);
              
              //set Sheet页内容
              setSheetContent1(xWorkbook, xSheet, uIds);
              
              xWorkbook.write(os);
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
	private void setSheetHeader1(XSSFWorkbook xWorkbook, XSSFSheet xSheet) {    	
          xSheet.setColumnWidth(0, 20 * 156);
          xSheet.setColumnWidth(1, 20 * 156);
          xSheet.setColumnWidth(2, 20 * 156);
          xSheet.setColumnWidth(3, 20 * 156);
          xSheet.setColumnWidth(4, 20 * 156);
          xSheet.setColumnWidth(5, 20 * 156);
          xSheet.setColumnWidth(6, 20 * 256);
          xSheet.setColumnWidth(7, 20 * 156);
                
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
          
          XSSFRow xRow0 = xSheet.createRow(0);
          
          XSSFCell xCell0 = xRow0.createCell(0);
          xCell0.setCellStyle(cs);
          xCell0.setCellValue("分公司");
          
          XSSFCell xCell1 = xRow0.createCell(1);
          xCell1.setCellStyle(cs);
          xCell1.setCellValue("营业所");
          
          XSSFCell xCell2 = xRow0.createCell(2);
          xCell2.setCellStyle(cs);
          xCell2.setCellValue("姓名");    
          
          XSSFCell xCell3 = xRow0.createCell(3);
          xCell3.setCellStyle(cs);
          xCell3.setCellValue("帐号");
          
          XSSFCell xCell4 = xRow0.createCell(4);
          xCell4.setCellStyle(cs);
          xCell4.setCellValue("职位");
          
          XSSFCell xCell5 = xRow0.createCell(5);
          xCell5.setCellStyle(cs);
          xCell5.setCellValue("手机"); 
          
          XSSFCell xCell6 = xRow0.createCell(6);
          xCell6.setCellStyle(cs);
          xCell6.setCellValue("组织");
          
          XSSFCell xCell7 = xRow0.createCell(7);
          xCell7.setCellStyle(cs);
          xCell7.setCellValue("状态");
      }  
      /**
       * set Sheet页内容
       */
      private void setSheetContent1(XSSFWorkbook xWorkbook, XSSFSheet xSheet,String[] uIds) {
          List<Emp> empList = dao.queryEmpListByUserid(uIds);
          CellStyle cs = xWorkbook.createCellStyle();
          cs.setWrapText(true);
          
          if (null != empList && empList.size() > 0) {
              for (int i = 0; i < empList.size(); i++) {
                  XSSFRow xRow = xSheet.createRow(i + 1);
                  Emp emp = empList.get(i);
                  for (int j = 0; j < 8; j++) {
                      XSSFCell xCell = xRow.createCell(j);
                      xCell.setCellStyle(cs);
                      switch (j) {
                          case 0:
                          	xCell.setCellValue(emp.getCompanyName());
                              break;                           
                          case 1:
                          	xCell.setCellValue(emp.getBranchName());
                              break;                           
                          case 2:
                          	xCell.setCellValue(emp.getuName());
                              break;                           
                          case 3:
                          	xCell.setCellValue(emp.getuId());
                              break;
                          case 4:
                              xCell.setCellValue(emp.getPosition());
                              break;
                          case 5:
                          	xCell.setCellValue(emp.getMobile());
                              break;
                          case 6:
                              xCell.setCellValue(emp.getdId());
                              break;
                          case 7:
                              xCell.setCellValue(emp.getStatus());
                              break;
                          default:
                              break;
                      }
                  }    
              }            
          }
      }

	@Override
	public PageResult queryPage(Integer currentPage, Integer numPerPage, String dId,String status) {
		// TODO Auto-generated method stub
		return dao.queryPage(currentPage, numPerPage, dId,status);
	}
   	
		
	
	//通过Department_id导出大数据的execl
	@Override
	public void exportFileBydId(HttpServletResponse response,String[] dIds,String status) {
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
            
            List<Emp> empList = dao.queryEmpListBydId(dIds,status);
                        
            if (null != empList && empList.size() > 0) {
            	int rowaccess=100;
            	int totle=empList.size();
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
                        Emp emp = empList.get(m);
                        for (int j = 0; j < 8; j++) {
                            Cell xCell = xRow.createCell(j);
                            
                            cs.setWrapText(true);  //是否自动换行
                            xCell.setCellStyle(cs);
                            
                            switch (j) {
                            	case 0:
        	                      	xCell.setCellValue(emp.getCompanyName());
        	                          break;                           
        	                    case 1:
        	                      	xCell.setCellValue(emp.getBranchName());
        	                          break;                                                 
                                case 2:
                                	xCell.setCellValue(emp.getuName());
                                    break;                           
                                case 3:
                                	xCell.setCellValue(emp.getuId());
                                    break;
                                case 4:
                                    xCell.setCellValue(emp.getPosition());
                                    break;
                                case 5:
                                	xCell.setCellValue(emp.getMobile());
                                    break;
                                case 6:
                                    xCell.setCellValue(emp.getdId());
                                    break;
                                case 7:
                                    xCell.setCellValue(emp.getStatus());
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
                
            }                                  
            xWorkbook.write(os);
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

    //登录
	@Override
	public boolean login(String loginId) {
		// TODO Auto-generated method stub
		return dao.login(loginId);
	}
	//判断用户名
	@Override
	public boolean uIdJudge(Emp emp) {
		// TODO Auto-generated method stub
		return dao.uIdJudge(emp);
	}
	
}
