package home.interfaces;

import com.mathworks.engine.MatlabEngine;
import home.classes.Matlab_MultiThread;

public interface Integration_Interface {
    double THRESHOLD = 0.000001;
    MatlabEngine engine = Matlab_MultiThread.getEngine();
    double shrinkDecimal(double value);
}
