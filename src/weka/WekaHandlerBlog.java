package weka;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * @author Rob van Emous
 * @author Mark van Doesum
 * @author Victor Lap
 * @date 2-12-2014
 */

public class WekaHandlerBlog {

	// Change this value to either explicitly apply pre-filtering (visible in
	// the .arff), or to apply it internally, when building the classifier.
	public static final boolean PREFILTERING = false;

	public static String trainPath = "blogstrain";
	public static String testPath = "blogstest";
	
	public static StringToWordVector filter = new StringToWordVector();

	public static void main(String[] args) {
		Instances trainData = buildARFF(new File(trainPath));
		Instances testData = buildARFF(new File(testPath));

		Instances newTrain;
		Instances newTest;
		try {
			
			filter.setInputFormat(trainData); // initializing the filter once with training set
			newTrain = Filter.useFilter(trainData, filter);  // configures the Filter based on train instances and returns filtered instances
			newTest = Filter.useFilter(testData, filter);
			writeARFF(newTrain, "train.arff");
			writeARFF(newTest, "test.arff");
			
			weka.classifiers.Classifier cs = PREFILTERING ? trainNaiveBayes(newTrain)
					: trainFilteredClassifier(newTrain);

			Evaluation ev = new Evaluation(newTrain);
			ev.evaluateModel(cs, newTest);
			System.out.println(ev.toSummaryString("\nResults\n=====\n", false));
			
			for(int i = 0; i < cs.getOptions().length; i++) {
				System.out.println(cs.getOptions()[i]);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private static void writeARFF(Instances data, String fileName) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(fileName, "UTF-8");
			writer.print(data);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * IF PREFILTERING then StringToWordVector is used 
	 * else raw data is generated with only content and class values as attributes
	 * @return a data set in either raw or filtered format
	 */
	public static Instances buildARFF(final File folder) {
		FastVector atts = new FastVector();
		FastVector classVal = new FastVector();
		FastVector nullValue = null;
		classVal.addElement("Male");
		classVal.addElement("Female");
		atts.addElement(new Attribute("content", nullValue));
		atts.addElement(new Attribute("@@class@@", classVal));
		Instances dataRaw = new Instances("GeneratedARFF", atts, 0);

		cascadeBuildARFF(dataRaw, folder);
		dataRaw.setClassIndex(dataRaw.numAttributes() - 1);

		if (PREFILTERING) {
		        System.out.println("FILTERING\n");
			Instances dataFiltered = applyTokenization(dataRaw);
			return dataFiltered;
		} else {
			System.out.println("NO FILTERING\n");
			return dataRaw;
		}
	}

	private static Instances applyTokenization(Instances dataRaw) {
		StringToWordVector filter = new StringToWordVector();
		try {
			filter.setInputFormat(dataRaw);
			Instances dataFiltered = Filter.useFilter(dataRaw, filter);
			dataFiltered.setClassIndex(0);
			return dataFiltered;
		} catch (Exception e) {
			return null;
		}
	}

	private static void cascadeBuildARFF(Instances dataRaw, final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				cascadeBuildARFF(dataRaw, fileEntry);
			} else {
				if (fileEntry.getName().contains("M")) {
					addFileAsContent(fileEntry, dataRaw, true);
				} else if (fileEntry.getName().contains("F")) {
					addFileAsContent(fileEntry, dataRaw, false);
				} else {
					System.out.println("Unkown file: " + fileEntry.getName());
				}
			}
		}
	}

	private static void addFileAsContent(File content, Instances data,
			boolean male) {
		double[] instanceValue = new double[data.numAttributes()];
		try {
			;
			instanceValue[0] = data.attribute(0).addStringValue(
					new Scanner(content).useDelimiter("\\A").next());
			instanceValue[1] = male ? 0 : 1;
			data.add(new Instance(1.0, instanceValue));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method uses StringToWordVector filter that is attached to the returned FilteredClassifier
	 * @return a trained FilteredClassifier that extends a NaiveBayes classifier
	 */
	public static FilteredClassifier trainFilteredClassifier(Instances data) {
		try {
			NaiveBayes classifierNaiveBayes = new NaiveBayes();			
			filter.setAttributeIndices("first");
			FilteredClassifier fClassifier = new FilteredClassifier();
			fClassifier.setClassifier(classifierNaiveBayes);
			fClassifier.setFilter(filter);
			fClassifier.buildClassifier(data);
			System.out.println(fClassifier);
			return fClassifier;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * THismethod does not use StringToWordVector filter
	 * @return NaiveBayes trained classifier
	 */
	public static NaiveBayes trainNaiveBayes(Instances data) {
		// train NaiveBayes and output model
		System.out.println("Training NaiveBayes");
		try {
			NaiveBayes classifierNaiveBayes = new NaiveBayes();
			classifierNaiveBayes.buildClassifier(data);
			System.out.println(classifierNaiveBayes);
			return classifierNaiveBayes;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
