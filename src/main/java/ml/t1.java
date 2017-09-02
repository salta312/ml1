package ml;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

import org.apache.poi.ss.usermodel.*;

public class t1 {
	public static void main(String[] args) {
		   Hashtable<Integer, Transaction> ht = new Hashtable<Integer, Transaction>();
		   DataFormatter formatter = new DataFormatter();

		   try {
	            FileInputStream excelFile = new FileInputStream(new File("my1.xlsx"));
	            Workbook workbook = new XSSFWorkbook(excelFile);
	            Sheet datatypeSheet = workbook.getSheet("0");
	            Iterator<Row> iterator = datatypeSheet.iterator();
	            int c = 0;
	            int tempNum;
	            Integer tempId;
	            Transaction tempTr;
	            String prevTransNum = null;
	            boolean init = true;
	            while (iterator.hasNext()) {

	                Row currentRow = iterator.next();
            			if(init) {
            				init = false;
            				continue;
            			}
	                Iterator<Cell> cellIterator = currentRow.iterator();
                 //   System.out.println(c);
	               // System.out.println("I am "+currentRow.getCell(0)+" and "+currentRow.getCell(3)+"customerID "+currentRow.getCell(6));
	                tempId = (int) currentRow.getCell(6).getNumericCellValue();
	                if(c == 0) {
	                		prevTransNum = formatter.formatCellValue(currentRow.getCell(0));
	                	//	c++;
	                }
	               // System.out.println(tempId);
	                if(tempId == 0) {
	                		continue;
	                }else {
	                	if (prevTransNum == formatter.formatCellValue(currentRow.getCell(0)) && c != 1) {
	                		tempTr = (Transaction) ht.get(tempId);
	                		tempTr.amount += currentRow.getCell(3).getNumericCellValue();
                			tempTr.price += currentRow.getCell(3).getNumericCellValue()* currentRow.getCell(5).getNumericCellValue(); 
	                	}
	                	else if(ht.containsKey(tempId)){
	                			tempTr = (Transaction) ht.get(tempId);
	                			if(prevTransNum != formatter.formatCellValue(currentRow.getCell(6))) {
	                				tempTr.transactionNum +=1;
	                			}
	                			tempTr.amount += currentRow.getCell(3).getNumericCellValue();
	                			tempTr.price += currentRow.getCell(3).getNumericCellValue()* currentRow.getCell(5).getNumericCellValue();
	                			ht.remove(tempId);
	                			ht.put(tempId, tempTr);
	                		}else {

	                			tempTr = new Transaction();
	                			tempTr.transactionNum = 1;
	                			tempTr.amount = (double) currentRow.getCell(3).getNumericCellValue();
	                			tempTr.price = (double) (currentRow.getCell(3).getNumericCellValue() * currentRow.getCell(5).getNumericCellValue());
	                			ht.put(tempId, tempTr);
	                		}
	                }
	                c++;

	            }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		   Enumeration<Integer> enumKeys = ht.keys();
		   Transaction tempT;
		   while(enumKeys.hasMoreElements()) {
			   Integer key = enumKeys.nextElement();
			   tempT = (Transaction) ht.get(key);
			//   System.out.println("I am "+key+" I have transuctions"+tempT.transactionNum+" and amount "+tempT.amount+" at a price "+tempT.price);
			//   System.out.println(tempT.amount);

		   }
		   normalise(ht);
		   

	    }
	public static void normalise(Hashtable<Integer, Transaction> ht) {
		ArrayList<Transaction> lst = new ArrayList<Transaction>(ht.values());
		Double[] transactionNum = new Double[lst.size()];
		Double[] boughtProducts = new Double[lst.size()];
		Double[] price = new Double[lst.size()];
		for(int i =0; i< lst.size(); i++) {
			transactionNum[i] = lst.get(i).transactionNum;
			boughtProducts[i] = lst.get(i).amount;
			price[i] = lst.get(i).price;
		}
		Arrays.sort(transactionNum);
		Arrays.sort(boughtProducts);
		Arrays.sort(price);
		   Enumeration<Integer> enumKeys = ht.keys();
		   Transaction tempT;
		   double tempTN =0;
		   double tempbP = 0;
		   double tempPrice = 0;
		   while(enumKeys.hasMoreElements()) {
			   Integer key = enumKeys.nextElement();
			   tempT = (Transaction) ht.get(key);
			   tempTN =  (tempT.transactionNum - transactionNum[transactionNum.length/2])/transactionNum[transactionNum.length-1];
			  // System.out.println(tempT.transactionNum+" minus "+transactionNum[transactionNum.length/2]+" devide by "+transactionNum[transactionNum.length-1]+" equals "+tempTN);
			   tempbP = (tempT.amount - boughtProducts[boughtProducts.length/2])/boughtProducts[boughtProducts.length - 1];
			   tempPrice = (tempT.price - price[price.length/2])/price[price.length - 1];
			   tempT.amount = tempbP;
			   tempT.price = tempPrice;
			   tempT.transactionNum = tempTN;
			   ht.remove(key);
			   ht.put(key, tempT);
			//   System.out.println(boughtProducts[boughtProducts.length - 1]+" and "+ boughtProducts[boughtProducts.length/2]);
			//   tempT.
			   //		   System.out.println("I am "+key+" I have transuctions"+tempT.transactionNum+" and amount "+tempT.amount+" at a price "+tempT.price);

		  // System.out.println(tempT.amount);
			//   System.out.println(tempT.amount);

		   }
		   KNN(ht);
		
	}
	public static void KNN(Hashtable<Integer, Transaction> ht) {
		//Random rnd = new Random();
		double minX = -1;
		double maxX = 1;
		double diffX = maxX - minX;
		double minY = -2;
		double maxY = 1;
		double diffY = maxY - minY;
		double m1X = minX + Math.random()*diffX;
		double m1Y = minY + Math.random()*diffY;
		double m2X = minX + Math.random()*diffX;
		double m2Y = minY + Math.random()*diffY;
		double m3X = minX + Math.random()*diffX;
		double m3Y= minY + Math.random()*diffY;
		Enumeration<Integer> enumKeys = ht.keys();
		Transaction tempT;
		double dist1, dist2, dist3;
		int count1 = 0;
		int count2 = 0; 
		int count3 = 0;
		for(int i =0; i<1; i++) {
		while(enumKeys.hasMoreElements()) {
			//System.out.println("I am here");
			 Integer key = enumKeys.nextElement();
			 tempT = (Transaction) ht.get(key);
			 dist1 = Math.pow(tempT.transactionNum- m1X + tempT.amount - m1Y, 2);
			 dist2 = Math.pow(tempT.transactionNum- m2X+ tempT.amount - m2Y, 2);
			 dist3 = Math.pow(tempT.transactionNum- m3X+ tempT.amount - m3Y, 2);
			// System.out.println(dist1+" "+dist2+" "+dist3);
			 if(dist1 <dist2 && dist1< dist3) {
				 tempT.col = 1;
			//	 System.out.println("I am 1");
			 }else if(dist2 <dist1 && dist2< dist3) {
				 tempT.col = 2;
			//	 System.out.println("I am 2");

			 }else if(dist3< dist1 && dist3< dist2){
				 tempT.col = 3;
			//	 System.out.println("I am 3");

			 }
			 ht.remove(key);
			 ht.put(key, tempT);
		}
		m1X = 0;
		m1Y = 0;
		m2X = 0;
		m2Y = 0;
		m3X = 0;
		m3Y = 0;
		enumKeys = ht.keys();
		while(enumKeys.hasMoreElements()) {
			Integer key = enumKeys.nextElement();
			 tempT = (Transaction) ht.get(key);
			 if(tempT.col == 1) {
				 m1X += tempT.transactionNum;
				 m1Y += tempT.amount;
				 count1 ++;
			 }else if(tempT.col == 2) {
				 m2X += tempT.transactionNum;
				 m2Y += tempT.amount;
				 count2 ++;
			 }else {
				 m3X += tempT.transactionNum;
				 m3Y += tempT.amount;
				 count3 ++;
			 }
		}
		if(count1 !=0) {
			m1X = m1X/count1;
			m1Y = m1Y/count1;
		}
		if(count2 != 0) {
			m2X = m2X/count2;
			m2Y = m2Y/count2;
		}
		if(count3 != 0) {
			m3X = m3X/count3;
			m3Y = m3Y/count3;
		}
		}
		try {
			FileOutputStream outputStream1X = new FileOutputStream("X1.dat");
			FileOutputStream outputStream2X = new FileOutputStream("X2.dat");
			FileOutputStream outputStream3X = new FileOutputStream("X3.dat");
			FileOutputStream outputStream1Y = new FileOutputStream("Y1.dat");
			FileOutputStream outputStream2Y = new FileOutputStream("Y2.dat");
			FileOutputStream outputStream3Y = new FileOutputStream("Y3.dat");

			byte[] btX;
			byte[] btY;
		enumKeys = ht.keys();
		while(enumKeys.hasMoreElements()) {
			Integer key = enumKeys.nextElement();
			 tempT = (Transaction) ht.get(key);
			 if(tempT.col == 1) {
				  btX = String.valueOf(tempT.transactionNum).getBytes();
				  outputStream1X.write(btX);
				  outputStream1X.write('\n');
				  btY = String.valueOf(tempT.amount).getBytes();
				  outputStream1Y.write(btY);
				  outputStream1Y.write('\n');
				 //System.out.println("I am here "+ tempT.transactionNum);
			 }else if(tempT.col == 2) {
				  btX = String.valueOf(tempT.transactionNum).getBytes();
				  outputStream2X.write(btX);
				  outputStream2X.write('\n');
				  btY = String.valueOf(tempT.amount).getBytes();
				  outputStream2Y.write(btY);
				  outputStream2Y.write('\n');
			 }else {
				  btX = String.valueOf(tempT.transactionNum).getBytes();
				  outputStream3X.write(btX);
				  outputStream3X.write('\n');
				  btY = String.valueOf(tempT.amount).getBytes();
				  outputStream3Y.write(btY);
				  outputStream3Y.write('\n');
			 }
			 
		}
		outputStream1X.close();
		outputStream1Y.close();
		outputStream2X.close();
		outputStream2Y.close();
		outputStream3X.close();
		outputStream3Y.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
