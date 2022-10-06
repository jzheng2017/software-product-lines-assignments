package spl.services; 

public  class  MessageTransformer{
	
    private ColorService colorService;	

    private void ColorMessageTransformer() {
        this.colorService = ColorService.getInstance();
    }

    public String transform(String msg) {
        return String.format("[%s] %s", colorService.getColor(), msg);
    }
}
