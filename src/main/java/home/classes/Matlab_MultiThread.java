package home.classes;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;

public class Matlab_MultiThread extends Thread {
    private static MatlabEngine engine;
    public static Matlab_MultiThread instance;

    public static Matlab_MultiThread getInstance() {
        if (instance == null) {
            instance = new Matlab_MultiThread();
        }
        return instance;
    }

    public void run() {
        try {
            engine = MatlabEngine.startMatlab();
        } catch (EngineException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static MatlabEngine getEngine() {
        return engine;
    }
}
