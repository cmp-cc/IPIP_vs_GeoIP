提示：IP地址每天可能都在发生变化，使用静态IP库更加不稳定。
#如何使用
## IPIP
IPIP 官网地址： http://www.ipip.net/

* 下载IP库 http://s.qdcdn.com/17mon/17monipdb.zip
* 下载解析代码 Java为例： https://github.com/17mon/java

** 使用案例 **

```
    static{
        IP.load("d:/17monipdb.dat");
    } 

    public static String[] ipFind(String IpAddress){
        return IP.find(IpAddress);
      } 
    
    public static void main(String[] args) {
        String[] item = ipFind("183.94.20.255");
        System.out.println("国家名:\t"+item[0]);
        System.out.println("省份名:\t"+item[1]);
        System.out.println("城市名:\t"+item[2]);
    }   

```

结果：

```
国家名:    中国
省份名:    湖北
城市名:    武汉  
```

##GeoIp2

GeoIp2 官网地址 ：

* 下载GeoIp库 http://geolite.maxmind.com/download/geoip/database/GeoLite2-City.mmdb.gz
* 假设你使用Maven构建项目（或者下载geoip2.jar http://central.maven.org/maven2/com/maxmind/geoip2/geoip2/2.3.0/geoip2-2.3.0.jar）
POM 文件增加

```
        <dependency>
            <groupId>com.maxmind.geoip2</groupId>
            <artifactId>geoip2</artifactId>
            <version>2.3.1</version>
        </dependency>  
```

** 使用案例 **

```
    /*GeoIp2 提供的本地MMDB数据库读取接口*/  
    private static  DatabaseReader reader;
    
    static {
        try {
            InputStream databaseStream = IPUtils.class.getClassLoader().getResourceAsStream("GeoLite2-City.mmdb");
            reader = new DatabaseReader.Builder(databaseStream).fileMode(FileMode.MEMORY).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }  

   /**
     *  获取IP地址的详细信息
     *  
     * @param ipAddress 输入IP地址，如:192.168.12.38
     * @return IP响应信息，包含详细地理信息
     */
    public static CityResponse getIPInfo(String ipAddress){
        CityResponse response = null;
         try {
             response = reader.city(InetAddress.getByName(ipAddress));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (GeoIp2Exception e) {
             /**
              *  有些IP地址GeoIP数据库中并不存在,这里应当异常处理
              **/
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
         return response;
    }  

    public static void main(String[] args) {
        CityResponse res = getIPInfo("183.94.20.255");
        try {
            System.out.println("大陆名:\t"+ res.getContinent().getName());
            System.out.println("国家名:\t"+res.getCountry().getName());
            System.out.println("国家名缩写:\t"+res.getCountry().getIsoCode());
            System.out.println("省份:\t"+res.getLeastSpecificSubdivision().getName());
            System.out.println("经度:\t"+res.getLocation().getLatitude());
            System.out.println("维度:\t"+res.getLocation().getLongitude());
            System.out.println("时区:\t"+res.getLocation().getTimeZone());
            System.out.println("中文省份名:\t"+res.getLeastSpecificSubdivision().getNames().get("ja"));
            System.out.println("城市名：\t"+res.getCity().getName());
            System.out.println("中文城市名:\t"+res.getCity().getNames().get("ja"));
            System.out.println("Traits info:\t"+res.getTraits());
            //等等
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }  

```

结果：

```
大陆名:    Asia
国家名:    China
国家名缩写:    CN
省份:    Hubei
经度:    30.5801
维度:    114.2734
时区:    Asia/Shanghai
中文省份名:    湖北省
城市名：    Wuhan
中文城市名:    武漢市
Traits info:    Traits [ipAddress=183.94.20.255, anonymousProxy=false, satelliteProvider=false, ]  

```


#比较

对1W 个真实有效IP进行测试

测试程序输入结果如下：

```
100个IP地址的 IPIP 共耗时：    6.0    比率:    16.666666666666668
100个IP地址的GeoIp 共耗时：    178.0    比率:    0.5617977528089888
1000个IP地址的 IPIP 共耗时：    15.0    比率:    66.66666666666667
1000个IP地址的GeoIp 共耗时：    106.0    比率:    9.433962264150944
10000个IP地址的 IPIP 共耗时：    83.0    比率:    120.48192771084338
10000个IP地址的GeoIp 共耗时：    532.0    比率:    18.796992481203006
100000个IP地址的 IPIP 共耗时：    688.0    比率:    145.34883720930233
100000个IP地址的GeoIp 共耗时：    5076.0    比率:    19.70055161544523
100个IP地址的IPIP的国家流失个数:    0    比率:    0.0
100个IP地址的IPIP的省份流失个数:    0    比率:    0.0
100个IP地址的IPIP的城市流失个数:    38    比率:    0.38
100个IP地址的GeoIp的国家流失个数:    0    比率:    0.0
100个IP地址的GeoIp的省份流失个数:    21    比率:    0.21
100个IP地址的GeoIp的城市流失个数:    19    比率:    0.19
1000个IP地址的IPIP的国家流失个数:    0    比率:    0.0
1000个IP地址的IPIP的省份流失个数:    0    比率:    0.0
1000个IP地址的IPIP的城市流失个数:    339    比率:    0.339
1000个IP地址的GeoIp的国家流失个数:    2    比率:    0.002
1000个IP地址的GeoIp的省份流失个数:    235    比率:    0.235
1000个IP地址的GeoIp的城市流失个数:    232    比率:    0.232
10000个IP地址的IPIP的国家流失个数:    0    比率:    0.0
10000个IP地址的IPIP的省份流失个数:    0    比率:    0.0
10000个IP地址的IPIP的城市流失个数:    3217    比率:    0.3217
10000个IP地址的GeoIp的国家流失个数:    4    比率:    4.0E-4
10000个IP地址的GeoIp的省份流失个数:    2344    比率:    0.2344
10000个IP地址的GeoIp的城市流失个数:    2313    比率:    0.2313
100000个IP地址的IPIP的国家流失个数:    0    比率:    0.0
100000个IP地址的IPIP的省份流失个数:    0    比率:    0.0
100000个IP地址的IPIP的城市流失个数:    27310    比率:    0.2731
100000个IP地址的GeoIp的国家流失个数:    60    比率:    6.0E-4
100000个IP地址的GeoIp的省份流失个数:    21904    比率:    0.21904
100000个IP地址的GeoIp的城市流失个数:    21609    比率:    0.21609
100个非中国IP IPIP完整性测试，不完整数据为100个,比例:    1.0
100个非中国IP GeoIP 完整性测试，不完整数据为50个,比例:    0.5
1000个非中国IP IPIP完整性测试，不完整数据为1000个,比例:    1.0
1000个非中国IP GeoIP 完整性测试，不完整数据为482个,比例:    0.482
10000个非中国IP IPIP完整性测试，不完整数据为10000个,比例:    1.0
10000个非中国IP GeoIP 完整性测试，不完整数据为5051个,比例:    0.5051
100个中国IP IPIP完整性测试，不完整数据为0个,比例:    0.0
100个中国IP GeoIP 完整性测试，不完整数据为9个,比例:    0.09
1000个中国IP IPIP完整性测试，不完整数据为0个,比例:    0.0
1000个中国IP GeoIP 完整性测试，不完整数据为134个,比例:    0.134
10000个中国IP IPIP完整性测试，不完整数据为0个,比例:    0.0
10000个中国IP GeoIP 完整性测试，不完整数据为1446个,比例:    0.1446

```



** 查询速度（毫秒） **


 数量\类型| IPIP | GeoIP2 |
----|----
  100| 16.67% | 0.56% 
1000|  66.67% |  9.43% 
10000| 120.48% | 18.79% 
100000|145.34%|19.70%


** 中国地区 完整性测试 （流失率的意义不大了，省份不存在或省份名称等于国家名称都表示数据不完整）**  

 数量\类型| IPIP | GeoIP2 |
----|----
  100| 0.0% | 0.9% 
1000|  0.0% |  0.13% 
10000| 0.0% | 0.14% 


** 非中国地区 完整性测试 **

 数量\类型| IPIP | GeoIP2 |
----|----
  100| 1.0 | 0.50% 
1000|  1.0% |  0.48% 
10000| 1.0% | 0.50% 



#总结 （使用和测试）

 --- | IPIP | GeoIP2 |
----|----
 数据丰富性| 简略 | 详细
 查询速度|  略优 |  缓慢
 中国IP完整性| 优 | 好
 非中国IP完整性| 差 | 良好 


1. GeoIp2的内容丰富，包含各种详细信息，IPIP只包含国家、省份、城市。 如果要求丰富性，选择GeoIp
2. IPIP的查询速度要比GeoIp2快很多
3. IPIP对中国地区支持友好，城市丢失较少，GeoIP2的城市和省份丢失较多。
4. IPIP对非中国地区较差，如果只获取国家名还可以，想要获取城市名，几乎是不能用的。GeoIP2主要是省份城市丢失导致完整性较差，但能够使用。
5. GeoIP2数据库中会出现IP不存在情况，需要自己做异常捕获，IPIP库中不会存在IP不存在。

** 如果检查国内IP使用IPIP还可以，如果要检查国外IP，IPIP就无能为力了。 (如上并没有对比IP检查的正确性)**
