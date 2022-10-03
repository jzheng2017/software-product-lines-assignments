package spl.services;  

public   class   ColorService {
	
	
    private static ColorService colorService;

	

	
    private String color;

	

	

    private ColorService() {

    }

	

	

    public static ColorService getInstance() {
        if (colorService == null) {
            colorService = new ColorService();
        }

        return colorService;
    }

	

	

    public String getColor() {
        return this.color;
    }

	

	

    public void setColor(String color) {
        this.color = color;
    }


}
