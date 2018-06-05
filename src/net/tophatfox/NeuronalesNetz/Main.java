package net.tophatfox.NeuronalesNetz;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ml.technikum.at.nn.DigitImage;
import ml.technikum.at.nn.DigitImageLoadingService;

public class Main implements KeyListener {

	View v = null;

	int currentIndex = 0;
	ArrayList<DigitImage> data = null;
	NeuroNet nNet = null;

	double targetError = 0.005;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		new Main();
	}

	public Main() {

		// Lernen
		DigitImageLoadingService imgLoadingService = new DigitImageLoadingService("data/train-labels-idx1-ubyte.dat", "data/train-images-idx3-ubyte.dat");

		try {
			data = new ArrayList(imgLoadingService.loadDigitImages());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int maxImageSize = 0;

		for (DigitImage digitImage : data) {
			if (maxImageSize < digitImage.getData().length) {
				maxImageSize = digitImage.getData().length;
			}
		}

		//Ziffern von 0 - 9 
		nNet = new NeuroNet(maxImageSize, 10);

		System.out.println(nNet);

		LocalTime start = LocalTime.now();

		System.out.println("Start: " + start);

		int runs = 0, i;
		double runError = 0;
		int percent = 0;

		do {
			percent = 0;
			runError = 0;
			for (i = 0; i < data.size(); i++) {
				nNet.learn(data.get(i).getData(), convertDigitToArray(data.get(i).getLabel()));
				runError += nNet.getNetworkError();
			}
			runs++;
			runError = (runError / (double) i);
			System.out.println("Run " + runs + " error: " + runError);
		} while (runError > targetError);

		System.out.println("Learning ended after " + runs + " runs.");
		System.out.println("Error: " + (runError * 100.0f) + "%");

		LocalTime stop = LocalTime.now();

		System.out.println("Stop: " + stop);

		LocalTime tempTime = LocalTime.from(start);
		long hours = tempTime.until(stop, ChronoUnit.HOURS);
		tempTime = tempTime.plusHours(hours);

		long minutes = tempTime.until(stop, ChronoUnit.MINUTES);
		tempTime = tempTime.plusMinutes(minutes);

		long seconds = tempTime.until(stop, ChronoUnit.SECONDS);

		System.out.println("Learning time: " + hours + ":" + minutes + ":" + seconds);

		//Testen

		imgLoadingService = new DigitImageLoadingService("data/t10k-labels-idx1-ubyte.dat", "data/t10k-images-idx3-ubyte.dat");

		try {
			data = new ArrayList(imgLoadingService.loadDigitImages());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ConfusionMatrix cMatrix = new ConfusionMatrix(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" });

		for (DigitImage digitImage : data) {
			cMatrix.addToValue("" + convertToDigit(nNet.classify(digitImage.getData())), "" + digitImage.getLabel());
		}

		System.out.println(cMatrix);
		System.out.println("Test accuracy: " + (cMatrix.getAccuracy() * 100.0f) + "%");

		// Nachdem lernen und testen fertig sind
		v = new View();
		v.addKeyListener(this);

	}

	public double[] convertDigitToArray(int digit) {
		double[] array = new double[10];
		array[digit] = 1;
		return array;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			currentIndex--;
			if (currentIndex < 0) {
				currentIndex = data.size() - 1;
			}

			imageToGUI(data.get(currentIndex));

		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			currentIndex++;
			if (currentIndex >= data.size()) {
				currentIndex = 0;
			}

			imageToGUI(data.get(currentIndex));
		}

	}

	public int imageToGUI(DigitImage image) {
		v.setImg(image.getImage());
		double[] out = nNet.classify(image.getData());
		int i = convertToDigit(out);
		v.digit.setText("Classify: " + i + " Real: " + image.getLabel());
		return i;
	}

	public int convertToDigit(double[] out) {
		for (int i = 0; i < out.length; i++) {
			if (out[i] == 1) {
				return i;
			}
		}
		return -1;
	}

	public void printImgArray(double[] imgData) {
		int size = (int) Math.sqrt(imgData.length);
		for (int i = 0; i < imgData.length; i++) {
			if ((i % size) == 0) {
				System.out.println("");
			}
			System.out.print(imgData[i]);
		}
		System.out.println("");
	}

}
