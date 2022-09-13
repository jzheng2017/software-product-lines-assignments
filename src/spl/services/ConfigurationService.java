package spl.services;

public interface ConfigurationService {
	void addFeature(String feature, boolean val);
    void changeFeatureVal(String feature, boolean val);
    boolean isFeatureOn(String feature);
}
