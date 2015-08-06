��ʾ��IP��ַÿ����ܶ��ڷ����仯��ʹ�þ�̬IP����Ӳ��ȶ���
#1�����ʹ��
### 1.1��IPIP
IPIP ������ַ�� http://www.ipip.net/

* ����IP�� http://s.qdcdn.com/17mon/17monipdb.zip
* ���ؽ������� JavaΪ���� https://github.com/17mon/java

** ʹ�ð��� **

```
    static{
        IP.load("d:/17monipdb.dat");
    } 

    public static String[] ipFind(String IpAddress){
        return IP.find(IpAddress);
      } 
    
    public static void main(String[] args) {
        String[] item = ipFind("183.94.20.255");
        System.out.println("������:\t"+item[0]);
        System.out.println("ʡ����:\t"+item[1]);
        System.out.println("������:\t"+item[2]);
    }   

```

�����

```
������:    �й�
ʡ����:    ����
������:    �人  
```

###1.2�� GeoIp2

GeoIp2 ������ַ ��

* ����GeoIp�� http://geolite.maxmind.com/download/geoip/database/GeoLite2-City.mmdb.gz
* ������ʹ��Maven������Ŀ����������geoip2.jar http://central.maven.org/maven2/com/maxmind/geoip2/geoip2/2.3.0/geoip2-2.3.0.jar��
POM �ļ�����

```
        <dependency>
            <groupId>com.maxmind.geoip2</groupId>
            <artifactId>geoip2</artifactId>
            <version>2.3.1</version>
        </dependency>  
```

** ʹ�ð��� **

```
    /*GeoIp2 �ṩ�ı���MMDB���ݿ��ȡ�ӿ�*/  
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
     *  ��ȡIP��ַ����ϸ��Ϣ
     *  
     * @param ipAddress ����IP��ַ����:192.168.12.38
     * @return IP��Ӧ��Ϣ��������ϸ������Ϣ
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
              *  ��ЩIP��ַGeoIP���ݿ��в�������,����Ӧ���쳣����
              **/
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
         return response;
    }  

    public static void main(String[] args) {
        CityResponse res = getIPInfo("183.94.20.255");
        try {
            System.out.println("��½��:\t"+ res.getContinent().getName());
            System.out.println("������:\t"+res.getCountry().getName());
            System.out.println("���Ĺ�����:\t"+res.getCountry().getNames().get("zh-CN"));
            System.out.println("��������д:\t"+res.getCountry().getIsoCode());
            System.out.println("ʡ��:\t"+res.getLeastSpecificSubdivision().getName());
            System.out.println("����:\t"+res.getLocation().getLatitude());
            System.out.println("ά��:\t"+res.getLocation().getLongitude());
            System.out.println("ʱ��:\t"+res.getLocation().getTimeZone());
            System.out.println("����ʡ����:\t"+res.getLeastSpecificSubdivision().getNames().get("zh-CN"));
            System.out.println("��������\t"+res.getCity().getName());
            System.out.println("���ĳ�����:\t"+res.getCity().getNames().get("zh-CN"));
            System.out.println("Traits info:\t"+res.getTraits());  
            //�ȵ�
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }  

```

�����

```
��½��:    Asia
������:    China
���Ĺ�����:    �й�
��������д:    CN
ʡ��:    Hubei
����:    30.5801
ά��:    114.2734
ʱ��:    Asia/Shanghai
����ʡ����:    ����ʡ
��������    Wuhan
���ĳ�����:    �人
Traits info:    Traits [ipAddress=183.94.20.255, anonymousProxy=false, satelliteProvider=false, ] 

```


#2���Ƚ�
** ��1W ����ʵ��ЧIP���в��� **

   2.1 ��ѯ�ٶȲ��ԣ����ܲ��ԣ�
   2.2 ��ѯ���������ݵ������ʧ����������ҡ�ʡ�ݡ����У�
   2.3 �����Բ���
   2.4 ��ȷ�Բ���

���Գ������������£�

```
100��IP��ַ�� IPIP ����ʱ��    6.0    ����:    16.666666666666668
100��IP��ַ��GeoIp ����ʱ��    178.0    ����:    0.5617977528089888
1000��IP��ַ�� IPIP ����ʱ��    15.0    ����:    66.66666666666667
1000��IP��ַ��GeoIp ����ʱ��    106.0    ����:    9.433962264150944
10000��IP��ַ�� IPIP ����ʱ��    83.0    ����:    120.48192771084338
10000��IP��ַ��GeoIp ����ʱ��    532.0    ����:    18.796992481203006
100000��IP��ַ�� IPIP ����ʱ��    688.0    ����:    145.34883720930233
100000��IP��ַ��GeoIp ����ʱ��    5076.0    ����:    19.70055161544523
100��IP��ַ��IPIP�Ĺ�����ʧ����:    0    ����:    0.0
100��IP��ַ��IPIP��ʡ����ʧ����:    0    ����:    0.0
100��IP��ַ��IPIP�ĳ�����ʧ����:    38    ����:    0.38
100��IP��ַ��GeoIp�Ĺ�����ʧ����:    0    ����:    0.0
100��IP��ַ��GeoIp��ʡ����ʧ����:    21    ����:    0.21
100��IP��ַ��GeoIp�ĳ�����ʧ����:    19    ����:    0.19
1000��IP��ַ��IPIP�Ĺ�����ʧ����:    0    ����:    0.0
1000��IP��ַ��IPIP��ʡ����ʧ����:    0    ����:    0.0
1000��IP��ַ��IPIP�ĳ�����ʧ����:    339    ����:    0.339
1000��IP��ַ��GeoIp�Ĺ�����ʧ����:    2    ����:    0.002
1000��IP��ַ��GeoIp��ʡ����ʧ����:    235    ����:    0.235
1000��IP��ַ��GeoIp�ĳ�����ʧ����:    232    ����:    0.232
10000��IP��ַ��IPIP�Ĺ�����ʧ����:    0    ����:    0.0
10000��IP��ַ��IPIP��ʡ����ʧ����:    0    ����:    0.0
10000��IP��ַ��IPIP�ĳ�����ʧ����:    3217    ����:    0.3217
10000��IP��ַ��GeoIp�Ĺ�����ʧ����:    4    ����:    4.0E-4
10000��IP��ַ��GeoIp��ʡ����ʧ����:    2344    ����:    0.2344
10000��IP��ַ��GeoIp�ĳ�����ʧ����:    2313    ����:    0.2313
100000��IP��ַ��IPIP�Ĺ�����ʧ����:    0    ����:    0.0
100000��IP��ַ��IPIP��ʡ����ʧ����:    0    ����:    0.0
100000��IP��ַ��IPIP�ĳ�����ʧ����:    27310    ����:    0.2731
100000��IP��ַ��GeoIp�Ĺ�����ʧ����:    60    ����:    6.0E-4
100000��IP��ַ��GeoIp��ʡ����ʧ����:    21904    ����:    0.21904
100000��IP��ַ��GeoIp�ĳ�����ʧ����:    21609    ����:    0.21609
100�����й�IP IPIP�����Բ��ԣ�����������Ϊ100��,����:    1.0
100�����й�IP GeoIP �����Բ��ԣ�����������Ϊ50��,����:    0.5
1000�����й�IP IPIP�����Բ��ԣ�����������Ϊ1000��,����:    1.0
1000�����й�IP GeoIP �����Բ��ԣ�����������Ϊ482��,����:    0.482
10000�����й�IP IPIP�����Բ��ԣ�����������Ϊ10000��,����:    1.0
10000�����й�IP GeoIP �����Բ��ԣ�����������Ϊ5051��,����:    0.5051
100���й�IP IPIP�����Բ��ԣ�����������Ϊ0��,����:    0.0
100���й�IP GeoIP �����Բ��ԣ�����������Ϊ9��,����:    0.09
1000���й�IP IPIP�����Բ��ԣ�����������Ϊ0��,����:    0.0
1000���й�IP GeoIP �����Բ��ԣ�����������Ϊ134��,����:    0.134
10000���й�IP IPIP�����Բ��ԣ�����������Ϊ0��,����:    0.0
10000���й�IP GeoIP �����Բ��ԣ�����������Ϊ1446��,����:    0.1446

```



####��** 2.1 ��ѯ�ٶȣ����룩 **
���㷽ʽ�� ����/��ʱ = ÿ���׼������

 ����\����| IPIP | GeoIP2 
----|----
  100| 16.67% | 0.56% 
1000|  66.67% |  9.43% 
10000| 120.48% | 18.79% 
100000|145.34%|19.70%


#### ** 2.2A �й����� �����Բ��� ����ʧ�ʵ����岻���ˣ�ʡ�ݲ����ڻ�ʡ�����Ƶ��ڹ������ƶ���ʾ���ݲ�������**
���㷽ʽ�� IP��ַΪ�й�������ʡ�����Ʋ�Ϊ���ҹ������Ʋ�����ʡ�����Ƶ�����/���� = ��������

 ����\����| IPIP | GeoIP2 
----|----
  100| 1.0% | 0.91% 
1000|  1.0% |  0.87% 
10000| 1.0% | 0.86% 


#### ** 2.2B ���й����� �����Բ��� **
���㷽ʽ�� IP��ַΪ���й�������ʡ�����Ʋ�Ϊ���ҹ������Ʋ�����ʡ�����Ƶ�����/���� = ��������

 ����\����| IPIP | GeoIP2 
----|----
  100| 0.0% | 0.51% 
1000|  0.0% |  0.52% 
10000| 0.0% | 0.50% 

#### **2.3 ׼ȷ�Ȳ��� **
** ���㷽ʽ�� IPIP��ѯ���� != GeoIP��ѯ����  ��IP����������IP���ѯ�� ��ѯ�����Ľ����IPIP���Һ�GeoIP����ƥ�䡣 ��������׼ȷ�Ƚϸߡ�**
> eg: 192.0.0.1 IPIPΪ�й���GeoIPΪ���� ����IP��Ϊ������ ��IPIP����+1�� 10000IP��ѯ�������������Ϊ׼ȷ�Ƚϸߵġ�

`��100000��IP���в�ѯ����ȥʧЧIP�� ������Ĳ�ѯ���Ϊ100%��ͬ��
����IPIP�Է��й�������ʡ�ݼ�����֧�֡� ���Բ����л���ʡ�ݵľ�ȷ�ġ�`



#�ܽ� ��ʹ�úͲ��ԣ�

 --- | IPIP | GeoIP2 
----|----
 ���ݷḻ��| ���� | ��ϸ
 ��ѯ�ٶ�|  ���� |  ����
 �й�IP������| �� | ��
 ���й�IP������| �� | ���� 


1. GeoIp2�����ݷḻ������������ϸ��Ϣ��IPIPֻ�������ҡ�ʡ�ݡ����С� ���Ҫ��ḻ�ԣ�ѡ��GeoIp
2. IPIP�Ĳ�ѯ�ٶ�Ҫ��GeoIp2��ܶ�
3. IPIP���й�����֧���Ѻã����ж�ʧ���٣�GeoIP2�ĳ��к�ʡ�ݶ�ʧ�϶ࡣ
4. IPIP�Է��й������ϲ���ֻ��ȡ�����������ԣ���Ҫ��ȡ�������������ǲ����õġ�GeoIP2��Ҫ��ʡ�ݳ��ж�ʧ���������Խϲ���ܹ�ʹ�á�
5. GeoIP2���ݿ��л����IP�������������Ҫ�Լ����쳣����IPIP���в������IP�����ڡ�

** �������й�IPʹ��IPIP�����ԣ����Ҫ������IP��IPIP������Ϊ����,��ΪIPIP��ѯ�������й�ʡ�ݺͳ��С�**
