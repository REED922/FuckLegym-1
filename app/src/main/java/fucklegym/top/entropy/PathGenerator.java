package fucklegym.top.entropy;

import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PathGenerator {
    //设置跑步范围经纬度坐标
    private static double[] latitude;
    private static double[] longitude;
    private static double base_lat, base_lon;

    public static HashMap<String, HashMap<String, double[]>> RunMaps = new HashMap<String, HashMap<String, double[]>>(){
        {

            put("西南石油大学（成都校区）", new HashMap<String, double[]>(){{
                put("latitude", new double[]{30.826336,30.826387,30.826456,30.826524,30.826539,30.826544,30.826177,30.825855,30.82534,30.825409,30.825482,30.825477,30.825526,30.825556,30.825498,30.825587,30.825439,30.825439,30.825251,30.825206,30.825232,30.825284,30.825271,30.825316,30.825329,30.82531,30.825335,30.825368,30.8254,30.82549,30.825568,30.825942,30.826453,30.826478,30.826472,30.82642,30.826336,30.825484,30.825387,30.825387,30.825355,30.825431,30.825476,30.82525,30.825211,30.825166,30.825169,30.825214,30.825408});
                put("longitude", new double[]{104.187456,104.187465,104.18747,104.187459,104.187442,104.187385,104.18657
,104.186479,104.186352,104.185874,104.185533,104.185413,104.184973,104.18496,104.184944,104.184978,104.184918,104.184918,104.184865,104.18479,104.184677,104.18458,104.184489,104.184505,104.184362,104.184294,104.184196,104.184279,104.184121,104.184076,104.184083,104.184226,104.184399,104.184595,104.184948,104.184971,104.185174,104.184971,104.184911,104.184911,104.185302,104.185466,104.185646,104.186586,104.186804,104.186895,104.186845,104.187071
,104.187146});


                put("base", new double[]{30.826336,104.187456});
            }});
//"xx大学（xx校区）": {
//  "latitude": [],
//  "longitude": []
//}
        }
    };

    public static void getLocalMaps(SharedPreferences local_maps){
        String[] maps = new String[]{};
        Map<String, ?> all = local_maps.getAll();
        for(String str: all.keySet()){
            double[] attr = new double[]{};
            JSONArray latitude = JSON.parseObject((String) all.get(str)).getJSONArray("latitude");
            JSONArray longitude = JSON.parseObject((String) all.get(str)).getJSONArray("longitude");
            double[] latitude_double = latitude.toJavaObject(double[].class);
            double[] longitude_double = longitude.toJavaObject(double[].class);
            double[] base = new double[]{latitude_double[0], longitude_double[0]};
            HashMap<String, double[]> theMap = new HashMap<>();
            theMap.put("latitude", latitude_double);
            theMap.put("longitude", longitude_double);
            theMap.put("base", base);
            RunMaps.put(str, theMap);
        }
    }


    private static void setAttr(String map){
        HashMap<String, double[]> currentMap = RunMaps.get(map);
        latitude = currentMap.get("latitude");
        longitude = currentMap.get("longitude");
        base_lat = currentMap.get("base")[0];
        base_lon = currentMap.get("base")[1];
    }

//    public static ArrayList<Pair<Double,Double>> genPointsInUESTC(int count){
//        ArrayList<Pair<Double,Double>> points = new ArrayList<>();
//        Random rad = new Random(System.currentTimeMillis());
//        for(int i = 1;i<=count;i++){
//            points.add(new Pair(base_lat + rad.nextInt(10000) / 1000000.0/2.0,base_lon + rad.nextInt(10000) / 1000000.0/2.0));
//        }
//        return points;
//    }
    public static ArrayList<Pair<Double,Double>> genRegularRoutine(String map, double totalMile){
        int cycleMeter = 400;//操场一圈的长度
        int totalMeter = (int)(totalMile * 1000);
        int offset = 6;//经纬度随机偏移量
        setAttr(map);
        ArrayList<Pair<Double,Double>> points = new ArrayList<>();
        Random rad = new Random(System.currentTimeMillis());
        for(int j = 0;j <= totalMeter/cycleMeter;j ++){
            if(totalMeter/cycleMeter - j - 1 >= 0) {
                for (int i = 0; i < latitude.length; i++) {
                    points.add(new Pair(latitude[i] + rad.nextInt(offset) * 1e-5, longitude[i] + rad.nextInt(offset) * 1e-5));
                }
            }else {
                int lastMeter = totalMeter - j * cycleMeter;
                double rate = ((double) lastMeter)/((double)cycleMeter);
                Log.d("run_rate", "genRegularRoutine: " + rate);
                for (int i = 0; i < latitude.length*rate; i++) {
                    points.add(new Pair(latitude[i] + rad.nextInt(offset) * 1e-5, longitude[i] + rad.nextInt(offset) * 1e-5));
                }
            }
        }

        return points;
    }

}
