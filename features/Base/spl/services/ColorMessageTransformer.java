package spl.services;

public class ColorMessageTransformer implements MessageTransformer {
    private ColorService colorService;

    public ColorMessageTransformer() {
        this.colorService = ColorService.getInstance();
    }

    @Override
    public String transform(String msg) {
        return String.format("[%s] %s", colorService.getColor(), msg);
    }

}
