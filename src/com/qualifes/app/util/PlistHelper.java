package com.qualifes.app.util;

import android.content.Context;
import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListParser;


public class PlistHelper {
    private static PlistHelper inst = null;
    private static NSArray data;

    public static PlistHelper getInstance(Context context) {
        if (inst != null) {
            return inst;
        }
        inst = new PlistHelper();
        try {
            data = (NSArray) PropertyListParser.parse(context.getAssets().open("region.plist"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inst;
    }

    public String[] getProvince() {
        String[] list = new String[data.count()];
        for (int i = 0; i < data.count(); i++) {
            NSDictionary d = (NSDictionary) data.objectAtIndex(i);
            NSString name = (NSString) d.objectForKey("name");
            list[i] = name.toString();
        }
        return list;
    }

    public String[] getCity(int province) {
        NSDictionary dist = (NSDictionary) data.objectAtIndex(province);
        NSArray array = (NSArray) dist.objectForKey("all");
        String[] list = new String[array.count()];
        for (int i = 0; i < array.count(); i++) {
            NSDictionary d = (NSDictionary) array.objectAtIndex(i);
            NSString name = (NSString) d.objectForKey("name");
            list[i] = name.toString();
        }
        return list;
    }

    public String[] getTown(int province, int city) {
        NSArray array = ((NSArray) ((NSDictionary) data.objectAtIndex(province)).objectForKey("all"));
        array = (NSArray) ((NSDictionary) array.objectAtIndex(city)).objectForKey("arr");
        String[] list = new String[array.count()];
        for (int i = 0; i < array.count(); i++) {
            NSDictionary d = (NSDictionary) array.objectAtIndex(i);
            NSString name = (NSString) d.objectForKey("name");
            list[i] = name.toString();
        }
        return list;
    }

    public String getPosition(int province, int city, int town) {
        String result = "";
        NSDictionary dist = (NSDictionary) data.objectAtIndex(province);
        result += dist.objectForKey("name").toString();
        dist = (NSDictionary) ((NSArray) dist.objectForKey("all")).objectAtIndex(city);
        result += "  " + dist.objectForKey("name").toString();
        dist = (NSDictionary) ((NSArray) dist.objectForKey("arr")).objectAtIndex(town);
        result += "  " + dist.objectForKey("name").toString();
        return result;
    }

    public String getProvinceId(int province) {
        NSDictionary dist = (NSDictionary) data.objectAtIndex(province);
        return ((NSString) dist.objectForKey("id")).toString();
    }

    public String getCityId(int province, int city) {
        NSDictionary dist = (NSDictionary) data.objectAtIndex(province);
        dist = (NSDictionary) ((NSArray) dist.objectForKey("all")).objectAtIndex(city);
        return ((NSString) dist.objectForKey("id")).toString();
    }

    public String getTownId(int province, int city, int town) {
        NSDictionary dist = (NSDictionary) data.objectAtIndex(province);
        dist = (NSDictionary) ((NSArray) dist.objectForKey("all")).objectAtIndex(city);
        dist = (NSDictionary) ((NSArray) dist.objectForKey("arr")).objectAtIndex(town);
        return ((NSString) dist.objectForKey("id")).toString();
    }


    public int getProvinceIndex(String province) {
        for (int i = 0; i < data.count(); i++) {
            NSDictionary d = (NSDictionary) data.objectAtIndex(i);
            NSString id = (NSString) d.objectForKey("id");
            if (id.toString().equals(province)) {
                return i;
            }
        }
        return 2;
    }

    public int getCityIndex(String province, String city) {
        NSArray cityD = (NSArray) ((NSDictionary) data.objectAtIndex(0)).objectForKey("all");
        for (int i = 0; i < data.count(); i++) {
            NSDictionary d = (NSDictionary) data.objectAtIndex(i);
            NSString id = (NSString) d.objectForKey("id");
            if (id.toString().equals(province)) {
                cityD = (NSArray) d.objectForKey("all");
                break;
            }
        }

        for (int i = 0; i < cityD.count(); i++) {
            NSDictionary d = (NSDictionary) cityD.objectAtIndex(i);
            NSString id = (NSString) d.objectForKey("id");
            if (id.toString().equals(city)) {
                return i;
            }
        }
        return 0;
    }

    public int getTownIndex(String province, String city, String town) {
        NSArray cityD = (NSArray) ((NSDictionary) data.objectAtIndex(0)).objectForKey("all");
        for (int i = 0; i < data.count(); i++) {
            NSDictionary d = (NSDictionary) data.objectAtIndex(i);
            NSString id = (NSString) d.objectForKey("id");
            if (id.toString().equals(province)) {
                cityD = (NSArray) d.objectForKey("all");
                break;
            }
        }

        NSArray townD = (NSArray) ((NSDictionary) cityD.objectAtIndex(0)).objectForKey("arr");
        for (int i = 0; i < cityD.count(); i++) {
            NSDictionary d = (NSDictionary) cityD.objectAtIndex(i);
            NSString id = (NSString) d.objectForKey("id");
            if (id.toString().equals(city)) {
                townD = (NSArray) d.objectForKey("arr");
                break;
            }
        }


        for (int i = 0; i < townD.count(); i++) {
            NSDictionary d = (NSDictionary) townD.objectAtIndex(i);
            NSString id = (NSString) d.objectForKey("id");
            if (id.toString().equals(town)) {
                return i;
            }
        }
        return 0;
    }


}
