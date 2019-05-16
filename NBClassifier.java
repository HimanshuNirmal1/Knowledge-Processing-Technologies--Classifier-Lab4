
/**
 * Course Number 612
 * Lab 04 Naive Bayes Classfier for text
 * Himanshu Vinod Nirmal
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class NBClassifier {
	String[] trainingDocs;
	static ArrayList<Integer> trainingLabels = new ArrayList<Integer>();
	int totClasses;
	static int[] classCounts; // number of docs 
	String[] classStrings; // concatenated string 
	int[] classTokenCounts; // total number of tokens per class
	HashMap<String, Double>[] condProb;
	HashSet<String> dict;  

	public NBClassifier(String[] docs, int numC) throws IOException {
		trainingDocs = docs;
		totClasses = numC;
		classCounts = new int[totClasses];
		classStrings = new String[totClasses];
		classTokenCounts = new int[totClasses];
		condProb = new HashMap[totClasses];
		dict = new HashSet<String>();
		for (int i = 0; i < totClasses; i++) {
			classStrings[i] = "";
			condProb[i] = new HashMap<String, Double>();
		}
		for (int i = 0; i < trainingLabels.size(); i++) {
			classCounts[trainingLabels.get(i)]++;
			classStrings[trainingLabels.get(i)] += (trainingDocs[i] + " ");
		}
		for (int i = 0; i < totClasses; i++) {
			String[] tokens = classStrings[i].split(" ");
			classTokenCounts[i] = tokens.length;

			// collecting the counts
			for (String token : tokens) {
				dict.add(token);
				if (condProb[i].containsKey(token)) {
					double count = condProb[i].get(token);
					condProb[i].put(token, count + 1);
				} else
					condProb[i].put(token, 1.0);
			}
		}
				for (int i = 0; i < totClasses; i++) {
			Iterator<Map.Entry<String, Double>> iterator = condProb[i].entrySet().iterator();
			int vSize = dict.size();
			while (iterator.hasNext()) {
				Map.Entry<String, Double> entry = iterator.next();
				String token = entry.getKey();
				Double count = entry.getValue();
				count = (count + 1) / (classTokenCounts[i] + vSize);
				condProb[i].put(token, count);
			}
		}
	}

		/**
		 * classify: this method classifies a document.
		 * 
       * @param doc: the document which is classified
		 * 
		 */
/*
	public int classfiy(String doc) throws IOException {

		int label = 0;
		int vSize = dict.size();
		double[] score = new double[totClasses];
		int i=0;
		while(i<score.length)
		{
		
			score[i] = Math.log(classCounts[i] * 1.0 / trainingDocs.length);
			i++;
		}
		String[] tokens = doc.split(" ");
		int j=0;
		while(j<totClasses)
		 {
			for (String token : tokens) {
				if (!condProb[j].containsKey(token))
					score[j] += Math.log(1.0 / (classTokenCounts[j] + vSize));
				else
					score[j] += Math.log(condProb[j].get(token));
					
			}
			j++;
		}
		double maxScore = score[0];
		int k=0;
		while(k<score.length)
		{
		
			if (maxScore < score[k])
				label = k;
			k++;
		}

		return label;
	} */

		/**
		 * classifyAll: this method classifies all documents.
		 * 
       * @param doc: the document which is classified
		 * 
		 */

	public int classfiyAll(String doc) throws IOException {
		int label = 0;
		int vSize = dict.size();
		double[] score = new double[totClasses];
		int i=0;
		while(i<score.length)
		 {
			score[i] = Math.log(classCounts[i] * 1.0 / trainingDocs.length);
			i++;
		}
		String[] tokens = doc.split(" ");
		int j=0;
		while(j<totClasses)
		 {
			for (String token : tokens) {
				if (!condProb[j].containsKey(token))
					score[j] += Math.log(1.0 / (classTokenCounts[j] + vSize));
				else
					score[j] += Math.log(condProb[j].get(token));
					
			}
			j++;
		}
		double maxScore = score[0];
		int k=0;
		while(k<score.length)
		 {
			if (maxScore < score[k])
				label = k;
			k++;
		}

		return label;
	}



//test_file
	public static String parse_test(File file) throws IOException {

		String testDocs = new String();
		BufferedReader br = null;
		int itr = 0;
		String[] tokens = null;

		// Read file
		br = new BufferedReader(new FileReader(file));
		String line = "";
		String allLines = "";
		while ((line = br.readLine()) != null) {
			allLines += line + " ";
		}
//		tokens = allLines.split("[ `.,?!:;$%()\\^\\-\"'#/*+&{}\\d+_@=\\|><]+");
		tokens = allLines.split("[\" ()_,?:;%&-]+");
      
      testDocs = Arrays.toString(tokens);

		br.close();
		return testDocs;
	}

	public static String test(File file) throws IOException {
		return parse_test(file);
	}


//train_file

	public static String[] parse(File[] allFiles) throws IOException {

		String[] trainDocs = new String[allFiles.length];
		BufferedReader br = null;
		int itr = 0, len = 0;
		String[] tokens = null;

		// Read file
		while (len < allFiles.length) {
			br = new BufferedReader(new FileReader(allFiles[itr]));
			String line = "";
			String allLines = "";
			while ((line = br.readLine()) != null) {
				allLines += line + " ";
			}
			//tokens = allLines.split("[ `.,?!:;$%()\\^\\-\"'#/*+&{}\\d+_@=\\|><]+");
         tokens = allLines.split("[\" ()_,?:;%&-]+");
			
         String str = Arrays.toString(tokens);
			trainDocs[itr] = str;
			itr++;
			len++;
		}
		br.close();
		return trainDocs;
	}

		/**
		 * preprocess:this method preprocesses the training data 
		 * 
		 * @param trainDataFolder :Path to the training documents.
		 * @param numClass        :total number of classes.
		 * @param folderType      :Training or Testing data folder.
		 * @param folderName      :Positive or Negative data.
		 * 
		 */
	
	public static void preprocess(String trainDataFolder, int numClass, String[] folderType, String[] folderName)
			throws IOException {

		File[] allFiles;
		
		while (numClass != 0) {
			String srcFolder = trainDataFolder + "\\\\" + folderType[0] + "\\\\" + folderName[2 - numClass];
			File folder = new File(srcFolder);
			allFiles = folder.listFiles();
			int i=0;
			int j=0;
			if (!srcFolder.contains("pos")) {
				while(i<allFiles.length)
				{
					trainingLabels.add(1);
					i++;
				}
			} else {
				while(j<allFiles.length)
				{
					trainingLabels.add(0);
				j++;
				}
			}
			numClass--;
		}
	}
   
   
      /**
		 * train: this method trains the classifier
		 * 
		 * @param folderType: test or train folder
		 * @param folderName: pos or neg data.
		 * @param destFolder: folder where training & testing data resides.
		 * 
		 */
		  
  public static String[] train(String folderType, String[] folderName, String destFolder) throws IOException {
      
      String[] trainDocs = new String[trainingLabels.size()];

		
		File[] allFiles;
		String srcFolder = destFolder + "\\\\" + folderType + "\\\\" + folderName[0];
		
		File folder = new File(srcFolder);
		allFiles = folder.listFiles();

		String[] posDoc = parse(allFiles);
		int i=0;
		while(i<posDoc.length)
		 {
			trainDocs[i] = posDoc[i];
			i++;
		}

		srcFolder = "";
		srcFolder = destFolder + "\\\\" + folderType + "\\\\" + folderName[1];
		folder = new File(srcFolder);
		allFiles = folder.listFiles();

		String[] negDoc = parse(allFiles);
		int index = 0;
		int j=0;
		while(j<trainDocs.length)
			
		 {
			if (trainDocs[j] != null)
				index++;
			j++;
		}
		int k=0;
		while(k<negDoc.length)
			
		 {
			trainDocs[index] = negDoc[k];
			index++;
			k++;
		}
		return trainDocs;
	}

		/**
		 * calAccuracy:this method calculates the accuracy of the classifier.
		 * 
		 * @param result: filename to be tested
		 * @param total_docs: total testing documents.
		 * @param testData: accuracy of classifier
		 * @param folderName :folder pos or neg.
		 * 
		 */

public static double calAccuracy(ArrayList<Integer> result, int total_docs, ArrayList<Integer> testData,
			String folderName) throws IOException {

		int cmp1 = 0, truClass = 0;
		int cmp2 = 0;
		double accuracy = 0.0;
		while (cmp1 < result.size()) {
			if (testData.get(cmp1).intValue() == result.get(cmp2).intValue()) {
				truClass++;
			}
			cmp1++;
			cmp2++;
		}
		if (total_docs > truClass)
			accuracy = (truClass * 1.0) % total_docs;
		truClass = 0;
		System.out.println(
				"accurately classified " + accuracy + " " + folderName + " documents: " + accuracy + "/" + total_docs);
		System.out.println(result);
		return accuracy;
	}

	
	public static void main(String[] args) throws IOException {

		int numClass = 2;
		File[] allFiles = null;
		String[] folderName = { "pos", "neg" };
		String[] folderType = { "train", "test" };

		String destFolder = args[0];

		preprocess(destFolder, numClass, folderType, folderName);

		String[] trainDocs = new String[trainingLabels.size()];

		trainDocs = train(folderType[0], folderName, destFolder);
		NBClassifier nb = new NBClassifier(trainDocs, numClass);

		
		ArrayList<Integer> result = new ArrayList<Integer>();
		ArrayList<Integer> dataTest = new ArrayList<Integer>();
		double accuracy = 0.0;
		int allDocs = 0;
		for (int i = 0; i < numClass; i++) {
			String srcFolder = destFolder + "\\\\" + folderType[1] + "\\\\" + folderName[i];
			File folder = new File(srcFolder);
			System.out.println(srcFolder);
			allFiles = folder.listFiles();
			allDocs += allFiles.length;

			int len = 0;
			for (int j = 0; j < allFiles.length; j++) {
				if (srcFolder.contains("pos")) {
					dataTest.add(0);
				} else
					dataTest.add(1);
			}
			while (len < allFiles.length) {
				String test = test(allFiles[len]);
				result.add(nb.classfiyAll(test));
				len++;
			}
			accuracy += calAccuracy(result, allFiles.length, dataTest, folderName[i]);
			result.clear();
			dataTest.clear();
		}
		System.out.println();
		System.out.println("total accurate classified " + accuracy + " out of " + allDocs);
		System.out.println("NBClassifier acc = " + accuracy / numClass + "%");

	//	String testDoc = "Chinese Chinese Chinese Tokyo Japan Chinese";
	//	System.out.println();
	//	System.out.println(
		//		"Classified single given document as " + (nb.classfiy(testDoc) == 1 ? "Negative" : "Positive"));
	}
}