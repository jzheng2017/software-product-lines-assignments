package spl.services; 

public  class  ConsoleLogService  implements LogService {
	
    @Override
    public void write(String message) {
        System.out.println(message);
    }

	

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }


}
