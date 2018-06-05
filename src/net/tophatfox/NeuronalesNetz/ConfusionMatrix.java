package net.tophatfox.NeuronalesNetz;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ConfusionMatrix {

	private ArrayList<String> labels = new ArrayList<>();

	// Zeilen und Spalten
	private int confusionMatrix[][];

	ConfusionMatrix(ArrayList<String> labels) {
		Collections.sort(labels);
		this.labels = labels;
		confusionMatrix = new int[labels.size()][labels.size()];
	}

	ConfusionMatrix(String[] labels) {
		ArrayList<String> sortedLabels = new ArrayList<>(Arrays.asList(labels));
		Collections.sort(sortedLabels);
		this.labels = sortedLabels;
		confusionMatrix = new int[sortedLabels.size()][sortedLabels.size()];
	}

	public void setValue(String predictedLables, String realLables, int value) {
		confusionMatrix[labels.indexOf(predictedLables)][labels.indexOf(realLables)] = value;
	}

	public void addToValue(String predictedLables, String realLables) {
		confusionMatrix[labels.indexOf(predictedLables)][labels.indexOf(realLables)] ++;
	}

	public int getValue(String lable1, String lable2) {
		return confusionMatrix[labels.indexOf(lable1)][labels.indexOf(lable2)];
	}

	public double getAccuracy() {
		int correct = 0;
		int all = 0;

		for (int y = 0; y < confusionMatrix.length; y++) {
			for (int x = 0; x < confusionMatrix[y].length; x++) {
				if (x == y) {
					correct += confusionMatrix[y][x];
				}
				all += confusionMatrix[y][x];

			}
		}

		return new BigDecimal((double) correct / (double) all).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	@Override
	public String toString() {
		String str = "R \\ P";
		ArrayList<Integer> sizes = new ArrayList<>();

		for (String label : labels) {
			sizes.add(label.trim().length());
			str += "\t|" + label.trim();
		}

		str += "\n-----------------------------------------------------------------------------------------------";

		for (int i = 0; i < confusionMatrix.length; i++) {
			str += "\n" + labels.get(i).trim();
			for (int entry : confusionMatrix[i]) {
				str += "\t|" + entry;
			}
		}

		return str;
	}

}
