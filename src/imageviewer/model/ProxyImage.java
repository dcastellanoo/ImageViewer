package imageviewer.model;

import imageviewer.persistence.FileImageLoader;
import imageviewer.util.AccessCounter;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProxyImage implements Image{
    private final Image realImage;
    private final Integer hashCode;
    
    public ProxyImage(File file){
        this.realImage = new RealImage(file.getName(), this.getStream(file));
        this.hashCode = file.hashCode();
    }
       
    private InputStream getStream(File file) {
        try {
            return new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    } 

    @Override
    public String name() {
        return this.realImage.name();
    }

    @Override
    public InputStream stream() {
        final Integer accesses = AccessCounter.getInstance().increment(this.hashCode);
        System.out.printf("File '%s' accessed ´%d' times\n",
                this.name(),
                accesses);
        return this.realImage.stream();
    }
}
