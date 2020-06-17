import java.io.*;
import java.util.*;

public class InvertedIndexThread extends Thread{
	
	Map<String, Map<String, Integer>> wordDocMap;
	List<File> files;
	int start;
	int stop;

	public InvertedIndexThread(Map<String, Map<String, Integer>> wordDocMap,List<File> files, int start, int stop){
		this.wordDocMap = wordDocMap;
		this.files = files;
		this.start = start;
		this.stop = stop;
	}

	@Override
    public void run() {
    	for (int i = start; i < stop; i++){
    		processFile(files.get(i), wordDocMap);
    	}
    }

    private void processFile(File file, Map<String, Map<String, Integer>> wordDocMap){
    	try {
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String [] words = fileScanner
                        .nextLine()
                        .toLowerCase()
                        .split("[( ,.;:/<>\"?!)]+"); 
                
                for (String word: words) {
                    Map<String, Integer> docCountMap = wordDocMap.get(word);
					if(docCountMap == null) {
    					docCountMap = new TreeMap<>();
   				    	wordDocMap.put(word, docCountMap);
					}
					Integer currentCount = docCountMap.get(file.getAbsolutePath());
					if(currentCount == null) {
    					currentCount = 0;
					}
					docCountMap.put(file.getAbsolutePath(), currentCount + 1);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}