package fileEncryptor;

import com.sun.istack.internal.NotNull;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Encryptor {

    private File mFile;
    //TODO 使用树的结构存储
    private List<Video> mVideos = new LinkedList<>();
    private FileFilter fileFilter;



    public Encryptor(File file){
        mFile = file;
        fileFilter = new VideoFileFilter();
    }

    private void findAllFiles(File file){
        if (file == null){
            Log.loge("file 为 null");
            return;
        }

        if (!file.isDirectory()){
            if (fileFilter.accept(file)){
                mVideos.add(new Video(file.getTotalSpace(),file));
            }
        }else {
           File[] files =  file.listFiles(fileFilter);

            //findAllFiles();
        }
    }


    private void encyptOnFile(@NotNull File file){

        try {
            FileInputStream fi = new FileInputStream(file);
            BufferedInputStream bi = new BufferedInputStream(fi);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void toNewFile(){

    }



    private static class VideoFileFilter implements FileFilter{

        @Override
        public boolean accept(File pathname) {
            if (pathname.isDirectory())
                return true;

            String suffix = FileUtils.getSuffix(pathname);

            for (FileUtils.VideoType per : FileUtils.VideoType.values()){
                if (per.getName().equals(suffix)){
                    return true;
                }
            }

            return false;
        }
    }



}
