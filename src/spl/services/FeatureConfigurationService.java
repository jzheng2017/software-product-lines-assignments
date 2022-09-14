package spl.services;
import java.util.HashMap;

public class FeatureConfigurationService implements ConfigurationService {

	private static HashMap<String, Boolean> features = new HashMap<String, Boolean>();

	@Override
	public boolean isFeatureOn(String feature) {
		return features.get(feature);
	}
	
	@Override
	public void addFeature(String feature, Boolean val) {
		features.put(feature, val);
	}

}
