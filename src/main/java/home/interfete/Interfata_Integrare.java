package home.interfete;

import com.mathworks.engine.MatlabEngine;
import home.clase.Matlab_MultiThread;

import java.util.concurrent.ExecutionException;

public interface Interfata_Integrare {
    // Constanta pentru pragul de eroare
    double THRESHOLD = 0.000001;

    // Motorul Matlab folosit pentru calcule
    MatlabEngine engine = Matlab_MultiThread.getEngine();

    // Metoda pentru reducerea valorii la un numar cu mai putine zecimale
    double shrinkDecimal(double value);

    // Metoda pentru determinarea daca o functie este divergenta
    String isDivergent(String function) throws ExecutionException, InterruptedException;

    // Metoda pentru realizarea unui grafic al unei functii
    void plotting(String function, String min, String max, String plot_interval) throws ExecutionException, InterruptedException;
}

