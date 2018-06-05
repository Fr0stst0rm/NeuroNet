package net.tophatfox.NeuronalesNetz;

import java.util.Random;

public class Neuron {

	double learningRate = 0.2;
	double momentum = 0.9;

	boolean useMomentum = true;

	double value = 0;

	Neuron[] parentLayer = null;
	double[] parentWeights;
	double[] weightsChanges;

	double bias = 0;

	double biasWeight = 0;

	double error = 0;

	static Random random = new Random();

	public Neuron() {
	}

	public Neuron(Neuron[] parentLayer) {
		this.parentLayer = parentLayer;
		parentWeights = new double[this.parentLayer.length];
		weightsChanges = new double[this.parentLayer.length];
		resetWeights();
		resetBias();
	}

	public void setParentLayer(Neuron[] parentLayer) {
		this.parentLayer = parentLayer;
	}
	
	public void feedForward() {
		if (parentLayer != null) {
			double sum = 0;

			for (int i = 0; i < parentLayer.length; i++) {
				sum += (parentLayer[i].getValue() * parentWeights[i]);
			}

			sum += bias * biasWeight;
			
			value = 1.0f / (1.0f + Math.exp(-sum));
		}
	}

	public double getValue() {
		return value;
	}

//	public double getOutput() {
//		return 1.0 / (1.0 + Math.exp(-value));
//	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getHiddenLayerError(double outSum) {
		return error = outSum * getValue() * (1.0f - getValue());
	}

	public double getOutputError(double desiredoutput) {
		return error = (desiredoutput - value) * value * (1.0f - value);
	}

	public void adjustWeights() {
		double newWeight = 0;
		for (int i = 0; i < parentWeights.length; i++) {
			newWeight = learningRate * error * parentLayer[i].getValue();
			if (useMomentum) {
				parentWeights[i] += newWeight + momentum * weightsChanges[i];
				weightsChanges[i] = newWeight;
			} else {
				parentWeights[i] += newWeight;
			}
		}
	}

	public void adjustBiasWeight() {
		biasWeight += learningRate * error * bias;
	}

	public void resetWeights() {
		this.biasWeight = ((random.nextInt(2) == 1) ? -1.0f : 1.0f) * random.nextDouble();
		for (int i = 0; i < parentWeights.length; i++) {
			parentWeights[i] = ((random.nextInt(2) == 1) ? -1.0f : 1.0f) * random.nextDouble();
		}
	}

	//TODO bias random setzten oder nicht?
	public void resetBias() {
		bias = (random.nextInt(1) == 1) ? -1.0f : 1.0f;
	}

	public double getParentWeight(int index) {
		return parentWeights[index];
	}
	
	@Override
	public String toString() {
		return ""+value;
	}

}
