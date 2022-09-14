package spl.services;

public interface FeatureConfigurationService {
	void addFeature(String feature, boolean isOn);
    boolean isFeatureOn(String feature);
}
