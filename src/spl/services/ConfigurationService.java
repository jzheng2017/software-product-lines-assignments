package spl.services;

public interface ConfigurationService {
	void addFeature(String feature, Boolean val);
    boolean isFeatureOn(String feature);
}
