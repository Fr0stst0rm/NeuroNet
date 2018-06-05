package net.tophatfox.NeuronalesNetz;

public class NeuroNet {

	// 1 neuron per attribute
	Neuron[] inputLayer;

	// ~ squr(nIn * nOut)
	Neuron[] hiddenLayer;

	// 1 neuron per classifier
	Neuron[] outputLayer;

	double neworkError = 1;

	int correct = 0;
	int wrong = 0;

	public NeuroNet(int inputSize, int outputSize) {
		inputLayer = new Neuron[inputSize];
		hiddenLayer = new Neuron[(int) Math.ceil(Math.sqrt(inputSize * outputSize))];
		outputLayer = new Neuron[outputSize];

		for (int i = 0; i < inputLayer.length; i++) {
			inputLayer[i] = new Neuron();
		}

		for (int i = 0; i < hiddenLayer.length; i++) {
			hiddenLayer[i] = new Neuron(inputLayer);
		}
		for (int i = 0; i < outputLayer.length; i++) {
			outputLayer[i] = new Neuron(hiddenLayer);
		}
	}

	public void learn(double[] data, double[] desiredOut) {
		//Check input and output
		if (data.length != inputLayer.length) {
			throw new IndexOutOfBoundsException("Data size does not math inputLayer size!");
		}

		if (desiredOut.length != outputLayer.length) {
			throw new IndexOutOfBoundsException("DesiredOut size does not math outputLayer size!");
		}

		// Set Input
		for (int i = 0; i < inputLayer.length; i++) {
			inputLayer[i].setValue(data[i]);
		}

		// Feedforward
		for (int i = 0; i < hiddenLayer.length; i++) {
			hiddenLayer[i].feedForward();
		}
		for (int i = 0; i < outputLayer.length; i++) {
			outputLayer[i].feedForward();
		}

		// Backpropagation
		// Network Error
		double error = 0;
		for (int i = 0; i < outputLayer.length; i++) {
			error += Math.pow(outputLayer[i].getValue() - desiredOut[i], 2);
		}
		neworkError = error / outputLayer.length;

		// Calc output & hidden errors
		for (int i = 0; i < hiddenLayer.length; i++) {
			double sum = 0;
			for (int j = 0; j < outputLayer.length; j++) {
				sum += outputLayer[j].getOutputError(desiredOut[j]) * outputLayer[j].getParentWeight(i);
			}
			hiddenLayer[i].getHiddenLayerError(sum);
		}

		//Adjust Weights
		for (int i = 0; i < outputLayer.length; i++) {
			outputLayer[i].adjustWeights();
		}
		for (int i = 0; i < hiddenLayer.length; i++) {
			hiddenLayer[i].adjustWeights();
		}
		// Check output
		boolean correctOut = true;
		for (int i = 0; i < outputLayer.length; i++) {
			if (outputLayer[i].getValue() != desiredOut[i]) {
				correctOut = false;
				break;
			}
		}
		if (correctOut) {
			correct++;
		} else {
			wrong++;
		}
	}

	public double[] classify(double[] data) {
		//Check input and output
		if (data.length != inputLayer.length) {
			throw new IndexOutOfBoundsException("Data size does not math inputLayer size!");
		}

		// Set Input
		for (int i = 0; i < inputLayer.length; i++) {
			inputLayer[i].setValue(data[i]);
		}

		// Feedforward
		for (int i = 0; i < hiddenLayer.length; i++) {
			hiddenLayer[i].feedForward();
		}
		for (int i = 0; i < outputLayer.length; i++) {
			outputLayer[i].feedForward();
		}

		// Output Classification
		double maxValue = 0;
		int maxIndex = -1;
		double[] output = new double[outputLayer.length];
		for (int i = 0; i < outputLayer.length; i++) {
			if (maxValue < outputLayer[i].getValue()) {
				maxValue = outputLayer[i].getValue();
				maxIndex = i;
			}
		}

		output[maxIndex] = 1;

		return output;
	}

	public double getNetworkError() {
		return neworkError;
	}

	@Override
	public String toString() {
		String str = "";
		str += "Input layer: " + inputLayer.length + "\n";
		str += "Hidden layer: " + hiddenLayer.length + "\n";
		str += "Output layer: " + outputLayer.length;
		return str;
	}

}
