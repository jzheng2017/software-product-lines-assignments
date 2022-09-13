package spl.services;

public class FeatureConfigurationService implements ConfigurationService {

	private static boolean USE_COLORS = true;

	@Override
	public void setFeature(String feature, boolean val) {
		if(feature.equals("usernameColors")) {
			USE_COLORS = val;
		}
	}

	@Override
	public boolean isFeatureOn(String feature) {
		if(feature.equals("usernameColors")) {
			return USE_COLORS;
		}
		return false;
	}
}
