package home.interfaces;

import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;
import home.IntegrX;

public interface Integration {
    double THRESHOLD = 0.000001;
    MatlabEngine engine = IntegrX.getEngine();
    double shrinkDecimal(double value);
}
