import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InvertedIndexMap{
	 Map<String, Map<String, Integer>> wordDocMap;
	 File tokenFile;
	 List<File> files;
	 private double execTime;

	public InvertedIndexMap(File tokenFile, List<File> files){
		this.tokenFile = tokenFile;
		this.wordDocMap = new ConcurrentHashMap<>();
		this.files = files;
	}

	public void generateMapKeys() throws FileNotFoundException {
		Scanner scan = new Scanner(tokenFile);
        while (scan.hasNextLine()) {
            wordDocMap.put(scan.nextLine(), null);
        }
        scan.close();
	}

	public  void startThreads(int numOfThreads){
		long startTime = System.currentTimeMillis();
		Thread[] threads = new Thread[numOfThreads];
		for(int i = 0; i < numOfThreads; i++){
			threads[i] = new InvertedIndexThread(wordDocMap, files, 
												(files.size()/numOfThreads) * i, 
												i == (files.size() - 1) ? files.size() : (files.size()/numOfThreads) * (i + 1));
			threads[i].run();
		}
		for (int i = 0; i < numOfThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
		execTime = (double) (endTime - startTime) / 1000;
        filterMap();

	}

	public Map<String, Map<String, Integer>> getInvertedIndex(){
		return wordDocMap;
	}

	public double getExecutionTime(){
		return execTime;
	}


	private void filterMap(){
		Map<String, Map<String, Integer>> filteredWordDocMap = wordDocMap.entrySet()
												.stream()
												.filter(x -> !(x.getValue().isEmpty()))
												.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		wordDocMap = filteredWordDocMap;
	}



	public void generateOutputFile(){
        try {
            File output = new File("/Users/ievgenkhonenko/Desktop/vocabluary.txt");
            FileWriter outputWriter = new FileWriter("/Users/ievgenkhonenko/Desktop/vocabluary.txt");

            for(Map.Entry<String, Map<String,Integer>> wordDoc : wordDocMap.entrySet()){
    			String word = wordDoc.getKey();
    			outputWriter.write(word + ":\n");
    			Map<String, Integer> docWordCount = wordDoc.getValue();

   		    	for(Map.Entry<String, Integer> docFrequency : docWordCount.entrySet()) {
        			String document = docFrequency.getKey();
       		    	Integer wordCount = docFrequency.getValue();
                   	outputWriter.write("\t\t" + document +"\t" +"found " + wordCount + " times" + "\n");
    				}				
				}
            outputWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred :");
            e.printStackTrace();
        }

    }

}