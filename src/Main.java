import java.io.*;
import java.util.*;

//N = 2000

public class Main{
	public static void main(String[] args){
		System.out.println("Enter a path to the token file(.vocab extension) : ");
		File tokenFile = null;
		try {
			tokenFile = getFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Enter a path to the data folder : ");
		File dataFolder = null;
		try {
			dataFolder = getFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int numOfThreads = Integer.parseInt(args[0]);
		List<File> files = new ArrayList<>();
		getFiles(dataFolder,files);

		InvertedIndexMap map = new InvertedIndexMap(tokenFile, files);
		map.startThreads(numOfThreads);
		map.generateOutputFile();
		double execTime = map.getExecutionTime();
		System.out.println("Execution time (seconds) : "  + execTime);
		System.out.println("Find word: ");
		wordSearcher(map);

	}


	public static File  getFile() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String newPath = reader.readLine();
		File newFile = new File(newPath);
		return newFile;
	}

	public static void getFiles (File folder, List<File> fileList){
		for(File file : folder.listFiles()){
			if(file.isDirectory()){
				getFiles(file,fileList);
			}else{
				fileList.add(file);
			}
		}

	}

	public static void wordSearcher(InvertedIndexMap map){
		Map<String, Map<String, Integer>> curMap = map.getInvertedIndex();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String word = null;
            try {
                word = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(curMap.get(word));
        }
    }
}