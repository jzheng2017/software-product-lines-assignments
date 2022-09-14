package spl.services;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryFeatureConfigurationService implements FeatureConfigurationService {
	private final Map<String, Boolean> features = new HashMap<>();
    private final List<String> allowedFeatures = Arrays.asList("reverse", "rot13", "usernamecolors");

    @Override
	public boolean isFeatureOn(String feature) {
		return features.get(feature);
	}
	
	@Override
	public void addFeature(String feature, boolean isOn) {
		if (isValid(feature)) {
            features.put(feature, isOn);
        } //perhaps throw exception in the future if it is invalid
	}

    private boolean isValid(String feature) {
        return allowedFeatures.contains(feature);
    }
}
