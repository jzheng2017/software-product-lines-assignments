package spl.services;  

public    class   MessageTransformer {
	

    public String transform  (String msg) {
        return String.format("[%s] %s", colorService.getColor(), msg);
    }

	
	
    private ColorService colorService;

		

    private void ColorMessageTransformer() {
        this.colorService = ColorService.getInstance();
    }


}
