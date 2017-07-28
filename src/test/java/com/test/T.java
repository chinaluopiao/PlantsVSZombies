package com.test;

import java.io.*;

public class T {

    public static void main(String[] args) throws Exception {
        String dir1 = "/home/luopiao/src";

        String dir2 = "/home/luopiao/workspace/mygithub/PlantsVSZombies/src/main/java/";

        File file1 = new File(dir1);
        File[] files = file1.listFiles();


        for (File file : files) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"gbk"));
            PrintWriter pw = new PrintWriter(new File(dir2+file.getName()),"utf-8");
            String line = br.readLine();
            while (line!=null){
                System.out.println(line);
                pw.write(line+"\n");
                line = br.readLine();
            }
            br.close();
            pw.flush();
            pw.close();
        }

    }
}
