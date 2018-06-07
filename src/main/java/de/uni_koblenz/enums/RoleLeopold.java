package de.uni_koblenz.enums;

public enum RoleLeopold {
	ACTION(1.0), 
	BUSINESS_OBJECT(1.0), 
	OPTIONAL_INFORMATION_FRAGMENT(0.5), 
	SUBJECT(0.5);

private double weightedValue;

private RoleLeopold() {
	
}

private RoleLeopold(double weightedValue){
	this.weightedValue = weightedValue;
}

public double getWeightedValue() {
	return weightedValue;
}

public void setWeightedValue(double weightedValue) {
	this.weightedValue = weightedValue;
}

}