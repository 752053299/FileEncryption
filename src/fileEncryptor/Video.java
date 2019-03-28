package fileEncryptor;

import java.io.File;
import java.text.DecimalFormat;

public class Video {
    private long videoSize;
    private File videoFile;

    public Video(long videoSize, File videoFile) {
        this.videoSize = videoSize;
        this.videoFile = videoFile;
    }

    public long getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(long videoSize) {
        this.videoSize = videoSize;
    }

    public File getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(File videoFile) {
        this.videoFile = videoFile;
    }

    @Override
    public String toString() {
        return videoFile.getName() + " size:" + printSize(videoSize);
    }

    private String printSize(long size){
        DecimalFormat df = new DecimalFormat("0.00");
        float kb = 1024f;
        float M = kb*1024f;
        float G = M*1024f;

        if (size < kb){
            return size + " byte";
        }
        if (size < M){
            return df.format(size/kb) + " kB";
        }

        if (size < G){
            return df.format(size/M) + " M";
        }

        return df.format(size/G) + " G";
    }
}
