package home.classes;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

import java.util.concurrent.ExecutionException;

public class Matlab_MultiThread extends Thread {
    private static MatlabEngine engine;
    public static Matlab_MultiThread instance = null;

    public static Matlab_MultiThread getInstance() {
        if (instance == null) {
            instance = new Matlab_MultiThread();
        }
        return instance;
    }

    public void run() {
        try {
            engine = MatlabEngine.startMatlab();
            engine.eval("test = 1 + 1;");
            engine.eval("clear;");
        } catch (EngineException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (MatlabExecutionException e) {
            throw new RuntimeException(e);
        } catch (MatlabSyntaxException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static MatlabEngine getEngine() {
        return engine;
    }
}
