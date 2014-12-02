package main;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * A trainer for training classifications.
 * 
 * @author Mark & Rob
 */
public class Trainer {
	private Map<Classification, String[]> classifications;
	
	/**
	 * Creates a trainer without classifiers.
	 */
	public Trainer() {
		classifications = new HashMap<Classification, String[]>();
	}
	
	/**
	 * Gets all classifications of this trainer.
	 * 
	 * @return the classifications
	 */
	public List<Classification> getAllClassifications() {
		List<Classification> classifs = new ArrayList<>();
		for (Classification classification : classifications.keySet()) {
			classifs.add(classification);
		}
		return classifs;
	}
	
	/**
	 * Adds the classification to the trainer.
	 * 
	 * @param classification to add
	 * @param trainFiles the paths of the train files
	 */	
	public void addClassification(Classification classification, String[] trainFiles) {
		classifications.put(classification, trainFiles);
	}
	
	/**
	 * Adds the classification without train files to the trainer.
	 * 
	 * @param classification to add
	 */	
	public void addClassification(Classification classification) {
		classifications.put(classification, new String[]{});
	}
	
	/**
	 * Adds the train files to the classification.
	 * 
	 * @param classification to add train files to
	 * @param trainFiles to add
	 */	
	public void addTrainFiles(Classification classification, String[] trainFiles) {
		String[] oldfiles = classifications.get(classification);
		String[] newFiles = new String[trainFiles.length + oldfiles.length];
		System.arraycopy(oldfiles, 0, newFiles, 0, oldfiles.length);
		System.arraycopy(trainFiles, 0, newFiles, oldfiles.length, trainFiles.length);
		classifications.put(classification, newFiles);
	}
	
	/**
	 * Removes all train files from the classification.
	 * 
	 * @param classification to remove all train files from
	 */
	public void removeTrainFiles(Classification classification) {
		classifications.put(classification, new String[]{});
	}
	
	/**
	 * Removes the classification from the trainer.
	 * 
	 * @param classification to remove
	 */
	public void removeClassification(Classification classification) {
		classifications.remove(classification);
	}
	
	/**
	 * Trains the classification with all files in the train-path.
	 * 
	 * @param classification the classification to train
	 */	
	public void trainClassification(Classification classification) {
		for (String file : classifications.get(classification)) {
			Scanner scan;
			try {
				scan = new Scanner(new File(file)).useDelimiter("\\Z");
			} catch (FileNotFoundException e) {continue;}
			if (scan.hasNext()) {
				String line = scan.next();
				classification.addTrainData(Tools.tokenize(line));
			}
			scan.close();
		}				
	}
	
	/**
	 * Trains all classifications with all files in the train-paths.
	 */	
	public void trainAllClassifications() {
		for (Classification classification : classifications.keySet()) {
			trainClassification(classification);		
		}
	}
	
	/**
	 * Removes all words from all classifications which do not occur a 
	 * certain amount of times.<br>
	 * Only call this after done training.
	 * 
	 * @param minOccurence the amount of times
	 */	
	public void cleanClassifiers(int minOccurence) {
		for (Classification classification : classifications.keySet()) {
			classification.cleanData(minOccurence);
		}
	}
	
	/**
	 * Stores all classifications as .txt files named after the 
	 * classification-type.
	 * 
	 * @param path the path to store them (may be null)
	 */
	public void storeClassifications(String path) {
		PrintWriter writer = null;
		for (Classification classification : classifications.keySet()) {
			try {
				writer = new PrintWriter((path != null ? (path + "\\") : "") 
						+ classification.getType() + ".txt", "UTF-8");
			} catch (FileNotFoundException e) {
			} catch (UnsupportedEncodingException e) {}
			writer.println(classification.toWriteableString());
			writer.close();
		}
	}
	
	/**
	 * Loads all classifications from .txt files named after the 
	 * classification-type.
	 * 
	 * @param path the path to store them (may be null)
	 * @return whether all train data could be found.
	 */
	public boolean loadClassifications(String path) {
		boolean allFound = true;
		for (Classification classification : classifications.keySet()) {
			File trainData = new File((path != null ? (path + "\\") : "") 
					+ classification.getType() + ".txt");
			if (!trainData.exists()) {
				allFound = false;
			} else {
				classification = new Classification(classification.getType(), 
					Tools.createScanner(trainData));
			}
		}
		return allFound;
	}
	
	public static void main(String[] args) {
		boolean forceReplaceTrainData = true;
		Map<String, String> classifiers= new HashMap<>();
		classifiers.put("male", "blogstrain\\M");
		classifiers.put("female", "blogstrain\\F");
		Trainer trainer = new Trainer();
		for (String classifName : classifiers.keySet()) {
			trainer.addClassification(new Classification(classifName), 
					Tools.AddBaseToNames(classifiers.get(classifName) + "\\", 
							new File(classifiers.get(classifName)).list()));
		}
		if (forceReplaceTrainData || !trainer.loadClassifications(null)) {
			// not trained yet
			trainer.trainAllClassifications();
			trainer.storeClassifications(null);
		}
		BayesianClassifier bc = new BayesianClassifier(trainer.getAllClassifications(), 1);
		String folderName = "blogstest";
		System.out.println("----Testing classifiers----");
		for (String typeName : new File(folderName).list()) {
			String type = typeName.equals("F") ? "female" : "male";
			System.out.println("Testing: " + type);
			double right = 0d, wrong = 0d;
			for (String fileName : new File(folderName + "\\" + typeName).list()) {
				if (bc.classify(folderName + "\\" + typeName + "\\" + fileName).equals(type)) {
					right++;
				} else {
					wrong++;
				}
				System.out.println(fileName + "(" + typeName + "):\t" + (fileName.length() <= 11 ? "\t" : "") + 
						bc.classify(folderName + "\\" + typeName + "\\" + fileName));
			}	
			double good = (double)right / ((double)right + (double)wrong);
			System.out.println("Results - right: " + good + " wrong: " + (1d - good));
		}

		
	}
}
