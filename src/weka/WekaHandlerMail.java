package weka;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesianLogisticRegression;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * @author Rob van Emous
 * @author Mark van Doesum
 * @author Victor Lap
 * @date 2-12-2014
 */

public class WekaHandlerMail {

	public static String trainPath = "mailtrain";
	public static String testPath = "mailtest";
	
	public static void main(String[] args) throws IOException {
		Instances trainData = buildARFF(new File(trainPath));
		Instances testData = buildARFF(new File(testPath));
		
		try {
			StringToWordVector filter = createFilter(trainData);
			Instances train = Filter.useFilter(trainData, filter);
			Instances test = Filter.useFilter(testData, filter);
			
			writeARFF(train, "trainMail.arff");
			writeARFF(test, "testMail.arff");
			
			weka.classifiers.Classifier cs = trainLogisticRegression(train);

			Evaluation ev = new Evaluation(train);
			ev.evaluateModel(cs, test);
			System.out.println(ev.toSummaryString());
			System.out.println(ev.toClassDetailsString());
			System.out.println(ev.toMatrixString());
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
		
	public static NaiveBayes trainNaiveBayes(Instances data) {
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

	public static J48 trainJ48(Instances data) {
		try {
			J48 classifierJ48 = new J48();
			classifierJ48.buildClassifier(data);
			System.out.println(classifierJ48);
			return classifierJ48;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static BayesianLogisticRegression trainLogisticRegression(Instances data) {
		try {
			BayesianLogisticRegression classifierLogistic = new BayesianLogisticRegression();
			classifierLogistic.buildClassifier(data);
			System.out.println(classifierLogistic);
			return classifierLogistic;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void writeARFF(Instances data, String fileName) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(fileName, "UTF-8");
			writer.print(data);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static StringToWordVector createFilter(Instances train) {
		StringToWordVector filter = new StringToWordVector();
		String[] options = new String[7];
		options[6] = "-C";
		options[0] = "-I";
		options[1] = "-R 1,2,3";
		options[2] = "-O";
		options[3] = "-T";
		options[4] = "-N 0";
		options[5] = "-M 1";
		try {
			filter.setOptions(options);
			filter.setInputFormat(train);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return filter;
	}
	
	public static Instances buildARFF(final File folder) {
		try {
			TextDirectoryLoader dataRaw = new TextDirectoryLoader();
			dataRaw.setDirectory(folder);
			return dataRaw.getDataSet();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}	
}
