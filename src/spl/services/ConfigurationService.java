package spl.services;

public interface ConfigurationService {
    void setFeature(String feature, boolean val);
    boolean isFeatureOn(String feature);
}
