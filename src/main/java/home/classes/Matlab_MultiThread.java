package home.classes;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;

public class Matlab_MultiThread extends Thread {
    private static MatlabEngine engine;
    public static Matlab_MultiThread instance = null;

    // Metodă pentru obținerea unei singure instanțe a clasei
    public static Matlab_MultiThread getInstance() {
        if (instance == null) {
            instance = new Matlab_MultiThread();
        }
        return instance;
    }

    // Metodă care rulează firul de execuție pentru inițializarea motorului MATLAB
    public void run() {
        try {
            engine = MatlabEngine.startMatlab();
        } catch (EngineException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Metodă pentru a obține motorul MATLAB
    public static MatlabEngine getEngine() {
        return engine;
    }
}