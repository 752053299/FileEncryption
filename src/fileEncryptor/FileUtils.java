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
    public static void cutFile(File sourceFile,long perSize,File targetFilePath){
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

        AppExecutors.getInstance().singleThread().execute(new Runnable() {
            @Override
            public void run() {

            }
        });

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

        private AppExecutors.NormalCallback callback;
        private long nowSize;
        private long perSize;

        @NotNull
        private File source;
        @NotNull
        private File target;
        private FileOutputStream fo;
        private BufferedOutputStream bo;

        public CutFileRunnable(AppExecutors.NormalCallback callback,long perSize,@NotNull File sourceFile,@NotNull File targetFilePath){
            this.callback = callback;
            this.source = sourceFile;
            this.target = targetFilePath;
            this.perSize = perSize;
        }

        @Override
        public void run() {
            try {
                FileInputStream fi = new FileInputStream(source);
                BufferedInputStream bi = new BufferedInputStream(fi);
                long totalSize = source.length();
                fo = getFileOutStream(nowSize,totalSize,target);
                bo = new BufferedOutputStream(fo);

                int by = 0;
                while ((by = bi.read()) >-1){

                    bo.write(by);
                    nowSize++;
                    if (nowSize > perSize){//达到单个文件上限
                        closeStream(fo,bo);
                        fo = getFileOutStream(nowSize,totalSize,target);
                        bo = new BufferedOutputStream(fo);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
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


        private FileOutputStream getFileOutStream(long currentSize ,long perSize,File target) throws IOException{
            int index = 0;
            if (currentSize != 0){
                index = (int) (currentSize/perSize);
            }

            String fileName = target.getAbsolutePath() + index;
            FileOutputStream fi = new FileOutputStream(fileName);
            return fi;
        }


    }
}
