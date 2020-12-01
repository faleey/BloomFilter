package ch.fhnw.dist.bf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class FileHandler {
    
    public Set<String> readFilterList(String fileName) {
        Set<String> filterList = new HashSet<>();
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            File myObj = new File(classLoader.getResource(fileName).getFile());
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                filterList.add(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filterList;
    }
}
