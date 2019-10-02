package com.lemon.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.Value;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;



public class ExcelUtil {
	
	private static Logger logger=Logger.getLogger(ExcelUtil.class);
	public static Object [][] readExcel( String filePath, String sheetName,int startRowNum,int endRowNum,int startCellNum,int endCellNum) {
		InputStream iStream = null;
		//声明数组
		Object [][] datas = null;
		try {
			iStream = new FileInputStream(new File(filePath));
			Workbook workbook = WorkbookFactory.create(iStream);
			//根据表单名获取sheet对象
			Sheet sheet = workbook.getSheet(sheetName);
			datas = new Object[endRowNum-startRowNum+1][endCellNum-startCellNum+1];
			for(int i=startRowNum;i<=endRowNum;i++){
				//循环获取行
				Row row = sheet.getRow(i-1);
				//循环获取列元素并放入数组中
				for (int j = startCellNum; j <=endCellNum; j++) {
				Cell cell = row.getCell(j-1,MissingCellPolicy.CREATE_NULL_AS_BLANK);
				//设置单元格的值为string类型
				cell.setCellType(CellType.STRING);
				//取出单元格的值
				String value = cell.getStringCellValue();
				logger.info("第【"+i+"】行，第【"+j+"】列的值为：【"+value+"】");
				//将值放入数组
				datas[i-startRowNum][j-startCellNum]=value;
				logger.info("数组data["+(i-startRowNum)+"]["+(j-startCellNum)+"]=【"+value+"】");
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (iStream!= null) {
				try {
					iStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return datas;
		
		
	}

}
