package fileEncryptor;

import com.sun.istack.internal.NotNull;

import java.io.*;

public class FileUtils {

    public enum VideoType{
        AVI("avi"),
        MOV("mov"),
        WMV("wmv"),
        MKV("mkv"),
        RMVB("rmvb"),
        MP4("mp4");

        String name;

        VideoType(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum Size{
        KB(1024),
        M(1024*1024),
        G(1024*1024*1024);

        int size;

        Size(int size){
            this.size = size;
        }

        public long getSize() {
            return size;
        }
    }


    /**
     * 将file切成几块
     * 文件名是uuid加上序号
     * 放在一个文件夹中,文件夹名字是原文件
     * @param sourceFile 不能是文件夹
     * @param perSize 每个部分的大小
     * @param targetFilePath 目标文件夹
     */
    public static void cutFile(File sourceFile, long perSize, File targetFilePath, AppExecutors.NormalCallback<String> callback){
        if (sourceFile.isDirectory()){
            return;
        }
        String dirName = sourceFile.getName();
        if (dirName.length()==0) {
            dirName = "0_0";
        }else {
            dirName = dirName.substring(0,dirName.lastIndexOf("."));
        }

        File fileDirectory = new File(targetFilePath.getAbsolutePath() +"\\"+ dirName);
        if (fileDirectory.exists()){
            //已经存在相同名字的文件
            fileDirectory = new File(fileDirectory.getAbsolutePath() + "_1");
        }
        if (!fileDirectory.mkdirs()) {return;}

        File targetFile;
        if (sourceFile.length() > perSize){
            targetFile = fileDirectory;
        }else {
            targetFile = targetFilePath;
        }

        AppExecutors.getInstance().singleThread().execute(new CutFileRunnable(callback,perSize,sourceFile,targetFile));

    }


    private void mergeFile(File[] files){

    }


    /**
     * 获取扩展名
     * @param file
     * @return
     */
    public static String getSuffix(File file){
        String fileName = file.getName();
       return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }


    private static class CutFileRunnable implements Runnable {

        private AppExecutors.NormalCallback<String> callback;
        private long nowSize;
        private long perSize;

        @NotNull
        private File source;
        @NotNull
        private File target;
      //  private FileOutputStream fo;
      //  private BufferedOutputStream bo;

        public CutFileRunnable(AppExecutors.NormalCallback<String> callback,long perSize,@NotNull File sourceFile,@NotNull File targetFilePath){
            this.callback = callback;
            this.source = sourceFile;
            this.target = targetFilePath;
            this.perSize = perSize;
        }

        @Override
        public void run() {
            FileInputStream fi=null;
            FileOutputStream fo=null;
            try {
                fi = new FileInputStream(source);

                long totalSize = source.length();
                byte[] buffer = new byte[1024*100];
                String suffix = getSuffix(source);
                fo = getFileOutStream(nowSize,perSize,target,suffix);

                int length = 0;
                long perReadSize = 0;//每个小文件读的大小
                while ((length = fi.read(buffer)) >-1){

                    fo.write(buffer,0,length);
                    perReadSize = perReadSize + length;
                    if (perReadSize > perSize){//达到单个文件上限
                        nowSize = nowSize + perReadSize;
                        perReadSize = 0;
                        closeStream(fo);
                        fo = getFileOutStream(nowSize,perSize,target,suffix);
                    }
                }


                if (callback != null){
                    callback.onFinish("success");
                }

            } catch (IOException e) {
                callback.onFinish(e.getMessage());
                e.printStackTrace();
            }finally {
                closeInStream(fi);
                closeStream(fo);
            }
        }

        private void closeStream(FileOutputStream fo,BufferedOutputStream bo){
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

        private void closeStream(FileOutputStream fo){
            if (fo != null){
                try {
                    fo.close();
                } catch (IOException e) {
                    System.out.println("error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
//
        private void closeInStream(FileInputStream fi){
            if (fi != null){
                try {
                    fi.close();
                }catch (IOException e){
                    System.out.println("error" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }


        private FileOutputStream getFileOutStream(long currentSize ,long perSize,File target,String suffix) throws IOException{
            int index = 0;
            if (currentSize != 0){
                index = (int) (currentSize/perSize);
            }

            String fileName = target.getAbsolutePath() +"\\"+ index + "."+ suffix;
            FileOutputStream fo = new FileOutputStream(fileName);
            return fo;
        }

    }

    public static void main(String[] args) {
        File source = new File("D:\\book\\Java并发编程实战（中文版）.pdf");
        File target = new File("D:\\out");
        cutFile(source, Size.M.getSize()*4, target, new AppExecutors.NormalCallback<String>() {
            @Override
            public void onFinish(String backData) {
                System.out.println(backData);
            }
        });
    }
}
