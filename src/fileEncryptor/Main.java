package fileEncryptor;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            FileInputStream fi = new FileInputStream("D:\\book\\Java并发编程实战（中文版）.pdf");
            BufferedInputStream bi = new BufferedInputStream(fi);
            FileOutputStream fo = new FileOutputStream("D:\\out\\new.pdf");
            BufferedOutputStream bo = new BufferedOutputStream(fo);

            int byteRead;
            while ((byteRead = bi.read()) > -1){
                bo.write(byteRead);
            }
          //  closeInStream(fi,bi);
          //  closeOutStream(fo,bo);
        } catch (IOException e){
            System.out.println(e.getMessage() );
        }

    }

    private static void closeOutStream(FileOutputStream fo, BufferedOutputStream bo){
        if (fo != null && bo !=null){
            try {
                fo.close();
                bo.close();
            } catch (IOException e) {
                System.out.println("error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void closeInStream(FileInputStream fi,BufferedInputStream bi){
        if (fi != null && bi !=null){
            try {
                fi.close();
                bi.close();
            }catch (IOException e){
                System.out.println("error" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

  //  public static void
}
