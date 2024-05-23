package service;

import java.io.IOException;

public class TimeCrossException extends RuntimeException {

    public TimeCrossException(final String message) {
        super(message);
    }


}


//package service;
//
//import java.io.IOException;
//
//public class ManagerSaveException extends RuntimeException {
//    public ManagerSaveException(final String message) {
//        super(message);
//    }
//
//    public ManagerSaveException(String message, IOException e) {
//        super(message);
//    }
//}