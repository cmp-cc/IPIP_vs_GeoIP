package org.antiy.IPIP_vs_GeoIP;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.maxmind.db.Reader.FileMode;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

public class IPIP_vs_GeoIP {
	
	/*GeoIp2 提供的本地MMDB数据库读取接口*/
	private static  DatabaseReader reader;
	private static List<String> ipTestLibList = new ArrayList<String>(); //10W 有效IP地址
	static {
		BufferedReader bufferReader = null;
		try {
			InputStream databaseStream = IPIP_vs_GeoIP.class.getClassLoader().getResourceAsStream("GeoLite2-City.mmdb");
			reader = new DatabaseReader.Builder(databaseStream).fileMode(FileMode.MEMORY).build();
			
			IP.load("src/main/resources/17monipdb.dat");

			/*
			 *  读取文件 
			 */
		     bufferReader = new BufferedReader(
						new InputStreamReader(IPIP_vs_GeoIP.class.getClassLoader().getResourceAsStream("ipLib.txt")));
			String tempValue;
			while((tempValue=bufferReader.readLine())!=null){
				ipTestLibList.add(tempValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(bufferReader!=null){
				try {
					bufferReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 读取速度测试
	 */
	public void querySpeedTest(long endValue){
		long current = System.currentTimeMillis();
		iteratorCollection(endValue, new ITest() {
			public int test(String ipAddress) {
				IP.find(ipAddress);
				return 1;
			}
		});
		
		rate(" IPIP 共耗时：", current, endValue);
		
		current = System.currentTimeMillis();
		iteratorCollection(endValue, new ITest() {
			public int test(String ipAddress) {
				try {
					 reader.city(InetAddress.getByName(ipAddress));
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (GeoIp2Exception e) {
					// TODO Auto-generated catch block
					/**
					 * IP 未找到不捕获
					 */
//					e.printStackTrace();
				}
				return 1;
			}
		});
		
		rate("GeoIp 共耗时：", current, endValue);
	}
	
	/**
	 *  流失率
	 *  国家，省份，城市
	 */
	long ipMiss,provinceMiss,cityMiss;
	public void lossRateTest(long endValue){
		iteratorCollection(endValue, new ITest() {
			public int test(String ipAddress) {
				//IPIP 流失率计算
				String[] item = IP.find(ipAddress);
				if(item[0].equals("")) ipMiss++;
				if(item[1].equals("")) provinceMiss++;
				if(item[2].equals("")) cityMiss++;
//				else System.out.println(item[2]);
				return 1;
			}
		});
		lossRate("IPIP的国家流失个数:", ipMiss, endValue);
		lossRate("IPIP的省份流失个数:", provinceMiss, endValue);
		lossRate("IPIP的城市流失个数:", cityMiss, endValue);
		ipMiss=0;provinceMiss=0;cityMiss=0;
		
		iteratorCollection(endValue, new ITest() {
			public int test(String ipAddress) {
				//GeoIp 流失率计算
				try {
					CityResponse res = reader.city(InetAddress.getByName(ipAddress));
					if(res.getMostSpecificSubdivision().getName()==null) provinceMiss++;
					if(res.getCity().getName()==null) cityMiss++;
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (GeoIp2Exception e) {
					ipMiss++;
					provinceMiss++;
					cityMiss++;
				}
				return 1;
			}
		});
		lossRate("GeoIp的国家流失个数:", ipMiss, endValue);
		lossRate("GeoIp的省份流失个数:", provinceMiss, endValue);
		lossRate("GeoIp的城市流失个数:", cityMiss, endValue);
		
		ipMiss=0;provinceMiss=0;cityMiss=0;
	}
	
	/**
	 * 
	 * 
	 * 中国地区的信息完整度
	 *  国家名!=省份名
	 *  计算完整性后，流失率就不重要了。因为如果省份不存在。也算作不完整
	 * @param endValue
	 */
	long integrity = 0; 
	public void  integrityTest(long endValue,final boolean isChina){
		boolean runComplete = iteratorCollection(endValue, new ITest() {
				//IPIP 中国地区完整测试
				public int test(String ipAddress) {
					String[] item = IP.find(ipAddress);
					if(item[0].equals("中国")?!isChina:isChina){
						return 0;
					}
					if (item[0].equals(item[1])) {
						integrity++;
					}
					return 1;
				}
			});
		
		if(runComplete){
			System.out.println(endValue+"个"+(isChina?"中国":"非中国")+"IP IPIP完整性测试，不完整数据为"+integrity+"个,比例:\t"+(integrity/(double)endValue));
		}
		integrity = 0;
		 runComplete = iteratorCollection(endValue, new ITest() {
			 	public int test(String ipAddress) {
			 	
			 		CityResponse res;
			 		try {
			 			res = reader.city(InetAddress.getByName(ipAddress));
			 			String countryName = res.getCountry().getName();
			 			if(countryName==null){
			 				return 1;
			 			}
			 			if(countryName.equals("China")?!isChina:isChina){
				 			return 0;
				 		}
			 			if(res.getMostSpecificSubdivision().getName()==null){
			 				integrity++;
			 				return 1;
			 			}
			 			if(res.getCountry().getName().equals(res.getMostSpecificSubdivision().getName())){
			 				integrity++;
			 				return 1;
			 			}
			 		} catch (UnknownHostException e) {
			 			// TODO Auto-generated catch block
			 			e.printStackTrace();
			 		} catch (IOException e) {
			 			// TODO Auto-generated catch block
			 			e.printStackTrace();
			 		} catch (GeoIp2Exception e) {
			 			integrity++;
			 			return 1;
			 		}
			 		return 1;
			 	}
		 	});
		 if(runComplete){
		 		System.out.println(endValue+"个"+(isChina?"中国":"非中国")+"IP GeoIP 完整性测试，不完整数据为"+integrity+"个,比例:\t"+(integrity/(double)endValue));
		 }
		 integrity=0;
	}
	
	/**
	 * 
	 * @param endValue
	 * @param handlerTest 测试行为
	 * @return 是否符合测试数量。
	 */
	private boolean iteratorCollection(long endValue,ITest handlerTest){
		int i = 0;
		for(Iterator<String> iter=ipTestLibList.iterator();iter.hasNext();){
			if(i==endValue){
				return true;
			}
			i += handlerTest.test(iter.next());
		}
		return false;
	}
	
	
	public static void main(String[] args) {
		IPIP_vs_GeoIP vs = new IPIP_vs_GeoIP();
		/*对不同IP数目的查询速度测试*/
		int[] testData = new int[]{100,1000,10000,100000};
		for(int i:testData){
			vs.querySpeedTest(i);
		}
		for(int i:testData){
			vs.lossRateTest(i);
		}
		for(int i:testData){
			vs.integrityTest(i,false);
		}
		for(int i:testData){
			vs.integrityTest(i,true);
		}
	}
	/**
	 * 比率计算
	 * @param descInfo 描述  
	 * @param startTime 起始时间
	 * @param numberValue 数值
	 */
	private void rate(String descInfo,long startTime,long numberValue){
		double Yi_Xi = System.currentTimeMillis()-startTime;
		System.out.println(numberValue+"个IP地址的"+descInfo+"\t"+Yi_Xi+"\t比率:\t"+numberValue/Yi_Xi);
	}
	
	private void lossRate(String descInfo,long lossNumber ,long numberValue){
		System.out.println(numberValue+"个IP地址的"+descInfo+"\t"+lossNumber+"\t比率:\t"+(double)lossNumber/numberValue);
	}
	
	/**
	 * 公用的测试接口
	 */
	interface ITest{
		public int test(String ipAddress);
	}
}
