package home.interfaces;

import com.mathworks.engine.MatlabEngine;
import home.classes.Matlab_MultiThread;

import java.util.concurrent.ExecutionException;

public interface Integration_Interface {
    double THRESHOLD = 0.000001;
    MatlabEngine engine = Matlab_MultiThread.getEngine();
    double shrinkDecimal(double value);

    String isDivergent(String function) throws ExecutionException, InterruptedException;

    void plotting(String function, String min, String max, String plot_interval) throws ExecutionException, InterruptedException;

    void method_plotting(String function, String min, String max, String plot_interval) throws ExecutionException, InterruptedException;
}
