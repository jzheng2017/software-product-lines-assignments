package spl.services;

public class ConsoleLogService implements LogService {
    @Override
    public void write(String message) {
        //#if Logging
        System.out.println(message);
        //#endif
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
