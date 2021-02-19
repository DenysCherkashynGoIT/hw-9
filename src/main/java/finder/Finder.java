package finder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

abstract public class Finder<T> implements Findable<T> {
    private File source;

    public Finder(File source) {
        this.source = (source.exists()) ? source : null;
    }

    public Finder(String source) {
        this(new File(source));
    }

    public void setSource(File source) {
        if (source.exists()) {
            this.source = source;
        } else {
            System.err.println("Wrong source-file path for \"" + source.getPath() + "\"! Check your input, please");
        }
    }

    public void setSource(String source) {
        this.setSource(new File(source));
    }

    public File getSource() {
        return this.source;
    }

    abstract public T getMatches();

    abstract public Finder<T> find();

    public void print() {
        System.out.println(this);
    }

    public File printToFile(File destination) {
        if (!destination.exists()) {
            try {
                destination.getParentFile().mkdirs();
                destination.createNewFile();
            } catch (IOException e) {
                System.err.println("Wrong destination file path! Check it, please.");
                return null;
            }
        }
        try (FileWriter writer = new FileWriter(destination);
             BufferedWriter buffWriter = new BufferedWriter(writer)) {
            buffWriter.write(this.toString());
        } catch (IOException e) {
            System.err.println("Wrong destination file path! Check it, please.");
            return null;
        }
        return destination;
    }

    public File printToFile(String destination) {
        return this.printToFile(new File(destination));
    }
}
