package com.example.pointsofinterest;

import android.os.Environment;
import android.util.Printer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FileIO {

    private static List<PoiValues> POIList = new ArrayList<PoiValues>();

    public static List<PoiValues> getPOIList()
    {
        return POIList;
    }

    public static void poiSave()
    {
        PrintWriter printWriter = null;

        try
        {
            PrintWriter pw = new PrintWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/POI_File.csv"));

            for (PoiValues entry : POIList)
            {
                String newLine = entry.getName() + ',' + entry.getType() + ',' + entry.getDesc() + ',' + entry.getLongitude() + ',' + entry.getLatitude() + '\n';
                pw.print(newLine);
            }
            pw.close();

        } catch (IOException e)
        {
            throw new RuntimeException("Error Saving to File:", e);

        } finally
        {
            if (printWriter !=null) printWriter.close();
        }
    }

    public static void poiLoad()
    {
        BufferedReader b_reader = null;
        FileReader f_reader = null;

        try
        {
            POIList.clear();

            File poiFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/POI_File.csv");
            if (poiFile.exists())
            {
                f_reader = new FileReader(poiFile);
                b_reader = new BufferedReader(f_reader);
                String line = "";
                while ((line = b_reader.readLine()) !=null)
                {
                    String[] columns = line.split(",");

                    if (columns.length == 5)
                    {
                        String name = columns[0];
                        String type = columns[1];
                        String desc = columns[2];

                        String longString = columns[3];
                        double longitude = Double.parseDouble(longString);

                        String latString = columns[4];
                        double latitude = Double.parseDouble(latString);


                        PoiValues POI = new PoiValues(name, type, desc, longitude, latitude);
                        POIList.add(POI);
                    }
                }
            }
        } catch (IOException e)
        {
            throw new RuntimeException("Error Loading File:", e);

        } finally
        {
            if (b_reader !=null)
            {
                try
                {
                    b_reader.close();

                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
