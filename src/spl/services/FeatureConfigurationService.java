package spl.services;
import java.util.HashMap;

public class FeatureConfigurationService implements ConfigurationService {

	private static HashMap<String, Boolean> features = new HashMap<String, Boolean>();
	
	public FeatureConfigurationService() {
		addFeature("usernameColors", true);
		addFeature("encryptMessages", true);
	}
	
	@Override
	public void changeFeatureVal(String feature, boolean val) {
		features.replace(feature, val);
	}

	@Override
	public boolean isFeatureOn(String feature) {
		return features.get(feature);
	}
	
	@Override
	public void addFeature(String feature, boolean val) {
		features.put(feature, val);
	}
}
