package tests;

import fileEncryptor.FileUtils;
import fileEncryptor.Video;
import org.junit.Test;

import java.io.File;


public class VideoTest {

    @Test
    public void testFileSize(){
        File file = new File("F:\\123\\444\\ipz00462hhb.wmv.bt.xltd");
        Video video = new Video(file.length(),file);
        System.out.println(video.toString());
    }

    @Test
    public void testMakFile(){
        File source = new File("C:\\Users\\Hypnotist\\Desktop\\videoTest.mp4");
        File target = new File("C:\\Users\\Hypnotist\\Desktop\\out");
        FileUtils.cutFile(source, FileUtils.Size.G.getSize() ,target);
    }
}