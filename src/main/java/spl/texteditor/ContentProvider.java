package spl.texteditor; 

public  interface  ContentProvider {
	
    String getText();

	
    /**
     * @return Whether the text has been changed since the last time it was requested with {@link #getText()}
     */
    boolean isDirty();


}
